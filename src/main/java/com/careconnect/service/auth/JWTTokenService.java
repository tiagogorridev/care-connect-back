package com.careconnect.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.careconnect.config.JwtConfig;
import com.careconnect.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTTokenService {

    private final JwtConfig cfg;
    private final Algorithm alg;

    public JWTTokenService(JwtConfig cfg) {
        this.cfg = cfg;
        this.alg = Algorithm.HMAC256(cfg.getSecret());
    }

    public String generateAccessToken(User u) {
        long now = System.currentTimeMillis();
        return JWT.create()
                .withIssuer(cfg.getIssuer())
                .withSubject(u.getEmail())
                .withClaim("uid", u.getId())
                .withClaim("role", u.getTipo().name())
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + cfg.getAccessExpMs()))
                .sign(alg);
    }

    public String generateRefreshToken(User u) {
        long now = System.currentTimeMillis();
        return JWT.create()
                .withIssuer(cfg.getIssuer())
                .withSubject(u.getEmail())
                .withClaim("uid", u.getId())
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + cfg.getRefreshExpMs()))
                .sign(alg);
    }

    public DecodedJWT verify(String token) throws JWTVerificationException {
        return JWT.require(alg)
                .withIssuer(cfg.getIssuer())
                .build()
                .verify(token);
    }
}
