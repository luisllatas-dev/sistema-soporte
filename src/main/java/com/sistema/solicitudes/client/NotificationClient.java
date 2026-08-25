package com.sistema.solicitudes.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sistema.solicitudes.model.Solicitud;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${notification.service.url:http://localhost:8082}")
    private String notificationServiceUrl;

    @Async
    public void notificarCreacion(Solicitud solicitud) {
        try {
            String url = notificationServiceUrl + "/api/notifications/email/solicitud-creada";
            NotificationPayload payload = construirPayload(solicitud);
            restTemplate.postForEntity(url, payload, String.class);
            log.info("Notificación de creación enviada a notification-service para ticket #{}", solicitud.getId());
        } catch (Exception e) {
            log.warn("No se pudo enviar notificación de creación para ticket #{}: {}", solicitud.getId(), e.getMessage());
        }
    }

    @Async
    public void notificarActualizacion(Solicitud solicitud) {
        try {
            String url = notificationServiceUrl + "/api/notifications/email/solicitud-actualizada";
            NotificationPayload payload = construirPayload(solicitud);
            restTemplate.postForEntity(url, payload, String.class);
            log.info("Notificación de actualización enviada a notification-service para ticket #{}", solicitud.getId());
        } catch (Exception e) {
            log.warn("No se pudo enviar notificación de actualización para ticket #{}: {}", solicitud.getId(), e.getMessage());
        }
    }

    private NotificationPayload construirPayload(Solicitud s) {
        NotificationPayload p = new NotificationPayload();
        p.setSolicitudId(s.getId());
        p.setDescripcion(s.getDescripcion());
        p.setEstado(s.getEstado() != null ? s.getEstado().name() : "ABIERTA");
        p.setObservaciones(s.getObservaciones());

        if (s.getCliente() != null) {
            p.setNombreCliente(s.getCliente().getNombre());
            p.setEmailCliente(s.getCliente().getCorreoElectronico());
        }

        if (s.getTecnicoAsignado() != null) {
            p.setNombreTecnico(s.getTecnicoAsignado().getNombre());
            p.setEmailTecnico(s.getTecnicoAsignado().getEmail());
        }

        return p;
    }

    @Data
    public static class NotificationPayload {
        private Long solicitudId;
        private String emailCliente;
        private String nombreCliente;
        private String emailTecnico;
        private String nombreTecnico;
        private String descripcion;
        private String estado;
        private String observaciones;
    }
}
