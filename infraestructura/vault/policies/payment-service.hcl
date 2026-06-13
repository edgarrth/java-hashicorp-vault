path "secret/data/payment-processing/dev" { capabilities = ["read"] }
path "transit/encrypt/payment-card-key" { capabilities = ["update"] }
path "transit/decrypt/payment-card-key" { capabilities = ["update"] }
path "database/creds/payment-service-role" { capabilities = ["read"] }
path "pki/issue/payment-service" { capabilities = ["update"] }
