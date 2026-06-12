package com.sistema.solicitudes.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.solicitudes.exception.SolicitudNotFoundException;
import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;
import com.sistema.solicitudes.repository.ISolicitudRepository;
import com.sistema.solicitudes.service.interfaces.ISolicitudService;

/**
 * Implementación del servicio de solicitudes de soporte técnico.
 * Delega la persistencia de datos a la capa de repositorio.
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
        List<Solicitud> listaCompleta = solicitudRepository.obtenerTodas();
        if (estado == null) {
            return listaCompleta;
        }
        List<Solicitud> filtrada = new ArrayList<>();
        for (Solicitud s : listaCompleta) {
            if (s.getEstado() == estado) {
                filtrada.add(s);
            }
        }
        return filtrada;
    }

    @Override
    public Solicitud obtenerPorId(Long id) {
        return solicitudRepository.obtenerPorId(id)
                .orElseThrow(() -> new SolicitudNotFoundException(id));
    }

    @Override
    public Solicitud crear(Solicitud solicitud) {
        solicitud.setFechaCreacion(LocalDateTime.now());
        
        // Guardamos la solicitud temporalmente para que el repositorio le asigne un ID
        Solicitud guardada = solicitudRepository.guardar(solicitud);
        Long id = guardada.getId();

        // Asignar IDs a cliente y técnico si no los tienen, basándonos en el ID de la solicitud
        if (solicitud.getCliente().getId() == null) {
            solicitud.getCliente().setId(id * 100 + 1);
        }
        if (solicitud.getTecnicoAsignado().getId() == null) {
            solicitud.getTecnicoAsignado().setId(id * 100 + 2);
        }

        // Volvemos a guardar para persistir los IDs del cliente/técnico asignados
        return solicitudRepository.guardar(solicitud);
    }

    @Override
    public Solicitud actualizar(Long id, Solicitud solicitud) {
        Solicitud existente = obtenerPorId(id); // Lanza excepción si no existe
        solicitud.setId(id);
        solicitud.setFechaCreacion(existente.getFechaCreacion());
        solicitud.setFechaActualizacion(LocalDateTime.now());

        return solicitudRepository.guardar(solicitud);
    }

    @Override
    public Solicitud actualizarEstado(Long id, EstadoSolicitud estado) {
        Solicitud existente = obtenerPorId(id); // Lanza excepción si no existe
        existente.setEstado(estado);
        existente.setFechaActualizacion(LocalDateTime.now());
        
        return solicitudRepository.guardar(existente);
    }

    @Override
    public void eliminar(Long id) {
        if (!solicitudRepository.existe(id)) {
            throw new SolicitudNotFoundException(id);
        }
        solicitudRepository.eliminar(id);
    }
}
