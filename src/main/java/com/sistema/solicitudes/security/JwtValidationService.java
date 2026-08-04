package com.sistema.solicitudes.security;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado de validar tokens JWT recibidos desde los clientes
 * utilizando la misma clave secreta que el auth-service.
 */
@Service
@Slf4j
public class JwtValidationService {

    @Value("${jwt.secret:java_spring_boot_micro_servicio_auth_security_2026_project_solicitudes}")
    private String secretKey;

    /**
     * Extrae el email (subject) del token JWT.
     */
    public String extraerEmail(String token) {
        return extraerClaims(token).getSubject();
    }

    /**
     * Extrae el rol del token y lo convierte a una lista de GrantedAuthority para Spring Security.
     */
    public List<SimpleGrantedAuthority> extraerAutoridades(String token) {
        Claims claims = extraerClaims(token);
        String rol = claims.get("rol", String.class);
        if (rol == null) {
            return Collections.emptyList();
        }
        if (!rol.startsWith("ROLE_")) {
            rol = "ROLE_" + rol;
        }
        return List.of(new SimpleGrantedAuthority(rol));
    }

    /**
     * Valida la firma del token y que no haya expirado.
     */
    public boolean esTokenValido(String token) {
        try {
            Claims claims = extraerClaims(token);
            boolean valido = !claims.getExpiration().before(new java.util.Date());
            log.info("Token validado exitosamente para el subject: {}", claims.getSubject());
            return valido;
        } catch (Exception e) {
            log.error("Error al validar el token JWT: {}", e.getMessage(), e);
            return false;
        }
    }

    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
