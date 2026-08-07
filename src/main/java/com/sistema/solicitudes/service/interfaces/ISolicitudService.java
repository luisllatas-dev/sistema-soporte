package com.sistema.solicitudes.service.interfaces;

import java.util.List;

import com.sistema.solicitudes.dto.SolicitudRequestDTO;
import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;

/**
 * Interfaz del servicio de solicitudes de soporte técnico.
 * Define las operaciones de negocio disponibles.
 */
public interface ISolicitudService {

    /**
     * Obtiene todas las solicitudes registradas, opcionalmente filtradas por estado.
     */
    List<Solicitud> obtenerTodas(EstadoSolicitud estado);

    /**
     * Obtiene una solicitud por su identificador.
     */
    Solicitud obtenerPorId(Long id);

    /**
     * Registra una nueva solicitud de soporte técnico.
     */
    Solicitud crear(SolicitudRequestDTO dto);

    /**
     * Actualiza la información de una solicitud existente.
     */
    Solicitud actualizar(Long id, SolicitudRequestDTO dto);

    /**
     * Actualiza únicamente el estado de una solicitud existente.
     */
    Solicitud actualizarEstado(Long id, EstadoSolicitud estado);

    /**
     * Elimina una solicitud de soporte técnico.
     */
    void eliminar(Long id);

    /**
     * Obtiene las solicitudes de un cliente específico.
     */
    List<Solicitud> obtenerPorCliente(Long clienteId);

    /**
     * Obtiene las solicitudes asignadas a un técnico específico.
     */
    List<Solicitud> obtenerPorTecnico(Long tecnicoId);

    /**
     * Busca solicitudes por texto contenido en la descripción.
     */
    List<Solicitud> buscarPorDescripcion(String texto);

    /**
     * Obtiene las solicitudes pertenecientes al cliente por su correo.
     */
    List<Solicitud> obtenerMisSolicitudes(String emailCliente);

    /**
     * Obtiene las solicitudes asignadas al técnico por su email.
     */
    List<Solicitud> obtenerMisAsignaciones(String emailTecnico);
}
