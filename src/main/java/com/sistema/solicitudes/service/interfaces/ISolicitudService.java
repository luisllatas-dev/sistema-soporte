package com.sistema.solicitudes.service.interfaces;

import java.util.List;

import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;

/**
 * Interfaz del servicio de solicitudes de soporte técnico.
 * Define las operaciones de negocio disponibles.
 */
public interface ISolicitudService {

    /**
     * Obtiene todas las solicitudes registradas, opcionalmente filtradas por estado.
     * @param estado estado opcional de las solicitudes para filtrar (puede ser null)
     * @return lista de solicitudes encontradas
     */
    List<Solicitud> obtenerTodas(EstadoSolicitud estado);

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
     * Actualiza únicamente el estado de una solicitud existente.
     * @param id identificador de la solicitud
     * @param estado nuevo estado a asignar
     * @return la solicitud actualizada con el nuevo estado
     */
    Solicitud actualizarEstado(Long id, EstadoSolicitud estado);

    /**
     * Elimina una solicitud de soporte técnico.
     * @param id identificador de la solicitud a eliminar
     */
    void eliminar(Long id);
}
