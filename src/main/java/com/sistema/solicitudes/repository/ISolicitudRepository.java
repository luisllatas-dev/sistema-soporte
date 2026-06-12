package com.sistema.solicitudes.repository;

import java.util.List;
import java.util.Optional;

import com.sistema.solicitudes.model.Solicitud;

/**
 * Interfaz del repositorio de solicitudes.
 * Define las operaciones básicas de acceso a datos.
 */
public interface ISolicitudRepository {

    /**
     * Obtiene todas las solicitudes del repositorio.
     */
    List<Solicitud> obtenerTodas();

    /**
     * Obtiene una solicitud por su ID.
     */
    Optional<Solicitud> obtenerPorId(Long id);

    /**
     * Guarda (crea o actualiza) una solicitud.
     */
    Solicitud guardar(Solicitud solicitud);

    /**
     * Elimina una solicitud por su ID.
     */
    void eliminar(Long id);

    /**
     * Verifica si existe una solicitud con el ID dado.
     */
    boolean existe(Long id);
}
