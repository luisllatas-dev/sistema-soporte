package com.sistema.solicitudes.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * Cliente HTTP para comunicarse con el microservicio de autenticación (auth-service).
 */
@Component
@Slf4j
public class AuthServiceClient {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthServiceClient(@Value("${auth.service.url:http://localhost:8081}") String authServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.authServiceUrl = authServiceUrl;
    }

    /**
     * Envía la solicitud para registrar un usuario en el auth-service.
     */
    public void crearUsuario(String email, String password, String rol) {
        try {
            String url = authServiceUrl + "/api/auth/internal/crear-usuario";
            Map<String, String> request = new HashMap<>();
            request.put("email", email);
            request.put("password", password);
            request.put("rol", rol);

            log.info("Llamando a auth-service ({}) para registrar el email: {}", url, email);
            restTemplate.postForObject(url, request, Object.class);
            log.info("Usuario {} registrado exitosamente en auth-service", email);
        } catch (Exception e) {
            log.error("Error al comunicarse con auth-service para registrar el usuario {}: {}", email, e.getMessage());
            throw new RuntimeException("No se pudo crear la cuenta de acceso en el servicio de autenticación: " + e.getMessage(), e);
        }
    }

    /**
     * Envía la solicitud para actualizar el email y/o contraseña de un usuario en el auth-service.
     */
    public void actualizarUsuario(String emailOriginal, String nuevoEmail, String nuevaPassword) {
        try {
            String url = authServiceUrl + "/api/auth/internal/actualizar-usuario";
            Map<String, String> body = new HashMap<>();
            body.put("emailOriginal", emailOriginal);
            body.put("nuevoEmail", nuevoEmail);
            body.put("password", nuevaPassword);

            log.info("Llamando a auth-service ({}) para actualizar usuario: {} -> nuevo email: {}", url, emailOriginal, nuevoEmail);
            restTemplate.postForObject(url, body, Object.class);
            log.info("Cuenta de usuario {} actualizada exitosamente en auth-service", emailOriginal);
        } catch (Exception e) {
            log.error("Error al actualizar cuenta en auth-service para {}: {}", emailOriginal, e.getMessage());
        }
    }

    /**
     * Envía la solicitud para eliminar la cuenta de un usuario en el auth-service por su email.
     */
    public void eliminarUsuario(String email) {
        if (email == null || email.trim().isEmpty()) return;
        try {
            String url = authServiceUrl + "/api/auth/internal/eliminar-usuario?email=" + email;
            log.info("Llamando a auth-service para eliminar la cuenta del email: {}", email);
            restTemplate.delete(url);
            log.info("Cuenta de usuario {} eliminada exitosamente en auth-service", email);
        } catch (Exception e) {
            log.error("Error al eliminar la cuenta de usuario en auth-service para {}: {}", email, e.getMessage());
        }
    }
}
