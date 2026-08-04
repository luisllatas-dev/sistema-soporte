package com.sistema.solicitudes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;

/**
 * Repositorio JPA para la entidad Solicitud.
 * Spring Data JPA genera las consultas automáticamente según la firma del método.
 */
public interface ISolicitudRepository extends JpaRepository<Solicitud, Long> {

    /**
     * Busca solicitudes filtradas por estado.
     */
    List<Solicitud> findByEstado(EstadoSolicitud estado);

    /**
     * Busca el historial de solicitudes realizadas por un cliente específico.
     */
    List<Solicitud> findByClienteId(Long clienteId);

    /**
     * Busca todas las solicitudes asignadas a un técnico específico.
     */
    List<Solicitud> findByTecnicoAsignadoId(Long tecnicoId);

    /**
     * Busca solicitudes que contengan una palabra o texto específico en su descripción (sin distinguir mayúsculas/minúsculas).
     */
    List<Solicitud> findByDescripcionContainingIgnoreCase(String texto);

    /**
     * Elimina todas las solicitudes pertenecientes a un cliente.
     */
    void deleteByClienteId(Long clienteId);
}
