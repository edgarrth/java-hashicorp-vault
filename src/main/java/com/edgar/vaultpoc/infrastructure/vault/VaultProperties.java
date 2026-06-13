package com.edgar.vaultpoc.infrastructure.vault;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix="vault")
public record VaultProperties(String url,String token,String kvPath,String transitKey,String pkiRole,String dbRole){}
