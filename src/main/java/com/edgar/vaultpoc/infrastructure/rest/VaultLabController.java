package com.edgar.vaultpoc.infrastructure.rest;
import com.edgar.vaultpoc.domain.ports.SecretManagerPort;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/security/v1/vault-labs")
public class VaultLabController{
 private final SecretManagerPort vault; public VaultLabController(SecretManagerPort vault){this.vault=vault;}
 @GetMapping("/kv") public Map<String,Object> kv(){return vault.readKvSecrets();}
 @PostMapping("/transit/encrypt") public Map<String,String> encrypt(@RequestBody TextRequest r){return Map.of("ciphertext",vault.encrypt(r.text()));}
 @PostMapping("/transit/decrypt") public Map<String,String> decrypt(@RequestBody CipherRequest r){return Map.of("plaintext",vault.decrypt(r.ciphertext()));}
 @PostMapping("/pki/certificates") public Map<String,String> cert(@RequestBody CertificateRequest r){return Map.of("pemBundle",vault.issueCertificate(r.commonName()));}
 @GetMapping("/database/credentials") public SecretManagerPort.DatabaseCredentials dbCreds(){return vault.dynamicDatabaseCredentials();}
 public record TextRequest(String text){} public record CipherRequest(String ciphertext){} public record CertificateRequest(String commonName){}
}
