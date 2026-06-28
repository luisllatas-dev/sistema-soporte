package com.sistema.solicitudes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;

/**
 * Repositorio JPA para la entidad Solicitud.
 * Spring Data JPA genera la implementación automáticamente.
 */
public interface ISolicitudRepository extends JpaRepository<Solicitud, Long> {

    /**
     * Busca solicitudes filtradas por estado.
     * Spring Data JPA genera la query automáticamente a partir del nombre del método.
     */
    List<Solicitud> findByEstado(EstadoSolicitud estado);
}
