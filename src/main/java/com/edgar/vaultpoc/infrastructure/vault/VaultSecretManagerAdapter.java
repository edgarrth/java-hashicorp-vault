package com.edgar.vaultpoc.infrastructure.vault;

import com.edgar.vaultpoc.domain.ports.SecretManagerPort;
import org.springframework.http.*;import org.springframework.stereotype.Component;import org.springframework.web.client.RestClient;
import java.nio.charset.StandardCharsets;import java.util.*;

@Component
public class VaultSecretManagerAdapter implements SecretManagerPort {
  private final VaultProperties props; private final RestClient client;
  public VaultSecretManagerAdapter(VaultProperties props){this.props=props;this.client=RestClient.builder().baseUrl(props.url()).defaultHeader("X-Vault-Token",props.token()).build();}
  @SuppressWarnings("unchecked") public Map<String,Object> readKvSecrets(){Map res=client.get().uri("/v1/"+props.kvPath()).retrieve().body(Map.class);return (Map<String,Object>)((Map<String,Object>)res.get("data")).get("data");}
  public String encrypt(String plainText){String b64=Base64.getEncoder().encodeToString(plainText.getBytes(StandardCharsets.UTF_8));Map body=Map.of("plaintext",b64);Map res=client.post().uri("/v1/transit/encrypt/"+props.transitKey()).body(body).retrieve().body(Map.class);return ((Map<String,Object>)res.get("data")).get("ciphertext").toString();}
  public String decrypt(String cipherText){Map body=Map.of("ciphertext",cipherText);Map res=client.post().uri("/v1/transit/decrypt/"+props.transitKey()).body(body).retrieve().body(Map.class);String plain=((Map<String,Object>)res.get("data")).get("plaintext").toString();return new String(Base64.getDecoder().decode(plain),StandardCharsets.UTF_8);} 
  public String issueCertificate(String commonName){Map body=Map.of("common_name",commonName,"ttl","24h");Map res=client.post().uri("/v1/pki/issue/"+props.pkiRole()).body(body).retrieve().body(Map.class);Map data=(Map)res.get("data");return data.get("certificate")+"\n"+data.get("issuing_ca");}
  public DatabaseCredentials dynamicDatabaseCredentials(){Map res=client.get().uri("/v1/database/creds/"+props.dbRole()).retrieve().body(Map.class);Map data=(Map)res.get("data");Number lease=(Number)res.getOrDefault("lease_duration",0);return new DatabaseCredentials(data.get("username").toString(),data.get("password").toString(),lease.longValue());}
}
