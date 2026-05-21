package com.sistema.solicitudes.service.interfaces;

import java.util.List;

import com.sistema.solicitudes.model.Solicitud;

/**
 * Interfaz del servicio de solicitudes de soporte técnico.
 * Define las operaciones de negocio disponibles.
 */
public interface ISolicitudService {

    /**
     * Obtiene todas las solicitudes registradas.
     * @return lista de solicitudes
     */
    List<Solicitud> obtenerTodas();

    /**
     * Obtiene una solicitud por su identificador.
     * @param id identificador de la solicitud
     * @return la solicitud encontrada
     */
    Solicitud obtenerPorId(Long id);

    /**
     * Registra una nueva solicitud de soporte técnico.
     * @param solicitud datos de la nueva solicitud
     * @return la solicitud creada con su ID asignado
     */
    Solicitud crear(Solicitud solicitud);

    /**
     * Actualiza la información de una solicitud existente.
     * @param id identificador de la solicitud a actualizar
     * @param solicitud datos actualizados
     * @return la solicitud actualizada
     */
    Solicitud actualizar(Long id, Solicitud solicitud);

    /**
     * Elimina una solicitud de soporte técnico.
     * @param id identificador de la solicitud a eliminar
     */
    void eliminar(Long id);
}
