package com.careconnect.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.access-exp-ms}")
    private long accessExpMs;

    @Value("${jwt.refresh-exp-ms}")
    private long refreshExpMs;

    public String getSecret() { return secret; }
    public String getIssuer() { return issuer; }
    public long getAccessExpMs() { return accessExpMs; }
    public long getRefreshExpMs() { return refreshExpMs; }
}
