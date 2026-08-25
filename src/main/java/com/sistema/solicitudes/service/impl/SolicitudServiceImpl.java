package com.sistema.solicitudes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.solicitudes.dto.SolicitudRequestDTO;
import com.sistema.solicitudes.exception.ClienteNotFoundException;
import com.sistema.solicitudes.exception.SolicitudNotFoundException;
import com.sistema.solicitudes.exception.TecnicoNotFoundException;
import com.sistema.solicitudes.model.Cliente;
import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;
import com.sistema.solicitudes.model.Tecnico;
import com.sistema.solicitudes.repository.IClienteRepository;
import com.sistema.solicitudes.repository.ISolicitudRepository;
import com.sistema.solicitudes.repository.ITecnicoRepository;
import com.sistema.solicitudes.service.interfaces.ISolicitudService;

/**
 * Implementación del servicio de solicitudes de soporte técnico.
 * Recibe DTOs de entrada, resuelve las relaciones por ID y persiste mediante JPA.
 */
@Service
public class SolicitudServiceImpl implements ISolicitudService {

    private final ISolicitudRepository solicitudRepository;
    private final IClienteRepository clienteRepository;
    private final ITecnicoRepository tecnicoRepository;
    private final com.sistema.solicitudes.client.NotificationClient notificationClient;

    public SolicitudServiceImpl(ISolicitudRepository solicitudRepository,
                                 IClienteRepository clienteRepository,
                                 ITecnicoRepository tecnicoRepository,
                                 com.sistema.solicitudes.client.NotificationClient notificationClient) {
        this.solicitudRepository = solicitudRepository;
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.notificationClient = notificationClient;
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
    public Solicitud crear(SolicitudRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ClienteNotFoundException(dto.getClienteId()));
        Tecnico tecnico = tecnicoRepository.findById(dto.getTecnicoAsignadoId())
                .orElseThrow(() -> new TecnicoNotFoundException(dto.getTecnicoAsignadoId()));

        Solicitud solicitud = new Solicitud();
        solicitud.setDescripcion(dto.getDescripcion());
        solicitud.setEstado(dto.getEstado());
        solicitud.setObservaciones(dto.getObservaciones());
        solicitud.setCliente(cliente);
        solicitud.setTecnicoAsignado(tecnico);

        Solicitud guardada = solicitudRepository.save(solicitud);
        notificationClient.notificarCreacion(guardada);
        return guardada;
    }

    @Override
    public Solicitud actualizar(Long id, SolicitudRequestDTO dto) {
        Solicitud existente = obtenerPorId(id);

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ClienteNotFoundException(dto.getClienteId()));
        Tecnico tecnico = tecnicoRepository.findById(dto.getTecnicoAsignadoId())
                .orElseThrow(() -> new TecnicoNotFoundException(dto.getTecnicoAsignadoId()));

        existente.setDescripcion(dto.getDescripcion());
        existente.setEstado(dto.getEstado());
        existente.setObservaciones(dto.getObservaciones());
        existente.setCliente(cliente);
        existente.setTecnicoAsignado(tecnico);

        Solicitud actualizada = solicitudRepository.save(existente);
        notificationClient.notificarActualizacion(actualizada);
        return actualizada;
    }

    @Override
    public Solicitud actualizarEstado(Long id, EstadoSolicitud estado) {
        Solicitud existente = obtenerPorId(id);
        existente.setEstado(estado);
        Solicitud actualizada = solicitudRepository.save(existente);
        notificationClient.notificarActualizacion(actualizada);
        return actualizada;
    }

    @Override
    public void eliminar(Long id) {
        if (!solicitudRepository.existsById(id)) {
            throw new SolicitudNotFoundException(id);
        }
        solicitudRepository.deleteById(id);
    }

    @Override
    public List<Solicitud> obtenerPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNotFoundException(clienteId);
        }
        return solicitudRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Solicitud> obtenerPorTecnico(Long tecnicoId) {
        if (!tecnicoRepository.existsById(tecnicoId)) {
            throw new TecnicoNotFoundException(tecnicoId);
        }
        return solicitudRepository.findByTecnicoAsignadoId(tecnicoId);
    }

    @Override
    public List<Solicitud> buscarPorDescripcion(String texto) {
        return solicitudRepository.findByDescripcionContainingIgnoreCase(texto);
    }

    @Override
    public List<Solicitud> obtenerMisSolicitudes(String emailCliente) {
        return solicitudRepository.findByClienteCorreoElectronicoIgnoreCase(emailCliente);
    }

    @Override
    public List<Solicitud> obtenerMisAsignaciones(String emailTecnico) {
        return solicitudRepository.findByTecnicoAsignadoEmailIgnoreCase(emailTecnico);
    }
}
