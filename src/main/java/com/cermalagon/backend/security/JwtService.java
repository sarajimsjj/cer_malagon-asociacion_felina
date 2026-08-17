package com.cermalagon.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Genera y valida los JWT que usan las administradoras para autenticarse.
 * El secreto (app.jwt.secret) debe ser una cadena aleatoria larga (32+ caracteres);
 * con menos de eso HS256 rechaza la clave.
 */
@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secreto,
            @Value("${app.jwt.expiracion-minutos}") long expiracionMinutos
    ) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionMinutos * 60 * 1000;
    }

    public String generarToken(String nombreUsuario) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expiracionMs);

        return Jwts.builder()
                .subject(nombreUsuario)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(clave)
                .compact();
    }

    /** Devuelve el nombre de usuario del token, o null si no es válido o ha caducado. */
    public String extraerNombreUsuarioSiValido(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(clave)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
