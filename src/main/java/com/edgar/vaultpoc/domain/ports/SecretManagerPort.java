package com.edgar.vaultpoc.domain.ports;
import java.util.Map;
public interface SecretManagerPort {
    Map<String,Object> readKvSecrets();
    String encrypt(String plainText);
    String decrypt(String cipherText);
    String issueCertificate(String commonName);
    DatabaseCredentials dynamicDatabaseCredentials();
    record DatabaseCredentials(String username, String password, long leaseDurationSeconds) {}
}
