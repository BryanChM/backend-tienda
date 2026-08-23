package com.guepardosport.backend_tienda.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // En producción esto debe venir de application.properties / variable de entorno, no hardcodeado
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            "cambia-esta-clave-por-una-muy-larga-y-secreta-de-al-menos-32-caracteres".getBytes()
    );

    private final long EXPIRATION_MS = 1000 * 60 * 60 * 8; // 8 horas

    public String generarToken(String correo, String rol) {
        return Jwts.builder()
                .subject(correo)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

    public String extraerCorreo(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public String extraerRol(String token) {
        return (String) Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("rol");
    }

    public boolean esTokenValido(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}