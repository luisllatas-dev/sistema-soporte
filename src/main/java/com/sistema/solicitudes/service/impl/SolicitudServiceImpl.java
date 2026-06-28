package com.sistema.solicitudes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.solicitudes.exception.SolicitudNotFoundException;
import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;
import com.sistema.solicitudes.repository.ISolicitudRepository;
import com.sistema.solicitudes.service.interfaces.ISolicitudService;

/**
 * Implementación del servicio de solicitudes de soporte técnico.
 * Delega la persistencia de datos a la capa de repositorio JPA.
 */
@Service
public class SolicitudServiceImpl implements ISolicitudService {

    private final ISolicitudRepository solicitudRepository;

    /**
     * Constructor con inyección de dependencias del repositorio.
     */
    public SolicitudServiceImpl(ISolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Override
    public List<Solicitud> obtenerTodas(EstadoSolicitud estado) {
        if (estado == null) {
            return solicitudRepository.findAll();
        }
        return solicitudRepository.findByEstado(estado);
    }

    @Override
    public Solicitud obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new SolicitudNotFoundException(id));
    }

    @Override
    public Solicitud crear(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }

    @Override
    public Solicitud actualizar(Long id, Solicitud solicitud) {
        Solicitud existente = obtenerPorId(id); // Lanza excepción si no existe
        solicitud.setId(id);
        solicitud.setFechaCreacion(existente.getFechaCreacion());
        return solicitudRepository.save(solicitud);
    }

    @Override
    public Solicitud actualizarEstado(Long id, EstadoSolicitud estado) {
        Solicitud existente = obtenerPorId(id); // Lanza excepción si no existe
        existente.setEstado(estado);
        return solicitudRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        if (!solicitudRepository.existsById(id)) {
            throw new SolicitudNotFoundException(id);
        }
        solicitudRepository.deleteById(id);
    }
}
