#!/usr/bin/env bash
set -euo pipefail
export VAULT_ADDR=${VAULT_ADDR:-http://127.0.0.1:8200}
export VAULT_TOKEN=${VAULT_TOKEN:-root}

echo "Configuring KV v2..."
vault secrets enable -path=secret kv-v2 || true
vault kv put secret/payment-processing/dev \
  merchant.api-key="merchant-dev-key-from-vault" \
  jwt.issuer="secure-java-vault-poc" \
  feature.secure-payments="true"

echo "Configuring Transit..."
vault secrets enable transit || true
vault write -f transit/keys/payment-card-key || true

echo "Configuring PKI..."
vault secrets enable pki || true
vault secrets tune -max-lease-ttl=87600h pki
vault write -field=certificate pki/root/generate/internal common_name="payment.local" ttl=87600h > /tmp/payment_ca.crt || true
vault write pki/config/urls issuing_certificates="$VAULT_ADDR/v1/pki/ca" crl_distribution_points="$VAULT_ADDR/v1/pki/crl"
vault write pki/roles/payment-service allowed_domains="payment.local,localhost" allow_subdomains=true allow_localhost=true max_ttl="72h"

echo "Configuring database dynamic credentials..."
vault secrets enable database || true
vault write database/config/payment-postgres \
  plugin_name=postgresql-database-plugin \
  allowed_roles="payment-service-role" \
  connection_url="postgresql://{{username}}:{{password}}@postgres:5432/paymentdb?sslmode=disable" \
  username="vaultadmin" password="vaultadmin"
vault write database/roles/payment-service-role \
  db_name=payment-postgres \
  creation_statements="CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE payments TO \"{{name}}\";" \
  default_ttl="15m" max_ttl="1h"

echo "Configuring policy and app token..."
vault policy write payment-service /vault/policies/payment-service.hcl
vault token create -policy=payment-service -ttl=24h -field=token > /vault/payment-service.token

echo "Configuring audit..."
vault audit enable file file_path=/tmp/vault-audit.log || true

echo "Done. App token saved inside container: /vault/payment-service.token"
