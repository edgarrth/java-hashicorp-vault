package com.edgar.vaultpoc.infrastructure.db;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix="app")
public record AppProperties(String jdbcBaseUrl){}
