package com.sistema.solicitudes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.solicitudes.client.AuthServiceClient;
import com.sistema.solicitudes.dto.TecnicoRequestDTO;
import com.sistema.solicitudes.exception.TecnicoNotFoundException;
import com.sistema.solicitudes.model.Tecnico;
import com.sistema.solicitudes.repository.ITecnicoRepository;
import com.sistema.solicitudes.service.interfaces.ITecnicoService;

import org.springframework.transaction.annotation.Transactional;
import com.sistema.solicitudes.model.Solicitud;
import com.sistema.solicitudes.repository.ISolicitudRepository;

/**
 * Implementación del servicio de técnicos.
 */
@Service
public class TecnicoServiceImpl implements ITecnicoService {

    private final ITecnicoRepository tecnicoRepository;
    private final ISolicitudRepository solicitudRepository;
    private final AuthServiceClient authServiceClient;

    public TecnicoServiceImpl(ITecnicoRepository tecnicoRepository, ISolicitudRepository solicitudRepository, AuthServiceClient authServiceClient) {
        this.tecnicoRepository = tecnicoRepository;
        this.solicitudRepository = solicitudRepository;
        this.authServiceClient = authServiceClient;
    }

    @Override
    public List<Tecnico> obtenerTodos() {
        return tecnicoRepository.findAll();
    }

    @Override
    public Tecnico obtenerPorId(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new TecnicoNotFoundException(id));
    }

    @Override
    public Tecnico crear(TecnicoRequestDTO dto) {
        Tecnico tecnico = new Tecnico();
        tecnico.setNombre(dto.getNombre());
        tecnico.setEspecialidad(dto.getEspecialidad());
        tecnico.setEmail(dto.getEmail());
        Tecnico guardado = tecnicoRepository.save(tecnico);

        // Crear la cuenta de usuario en el auth-service con rol ROLE_TECNICO
        String pass = (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) ? dto.getPassword() : "Tecnico123#";
        authServiceClient.crearUsuario(dto.getEmail(), pass, "ROLE_TECNICO");

        return guardado;
    }

    @Override
    public Tecnico actualizar(Long id, TecnicoRequestDTO dto) {
        Tecnico existente = obtenerPorId(id);
        String emailOriginal = existente.getEmail();

        existente.setNombre(dto.getNombre());
        existente.setEspecialidad(dto.getEspecialidad());
        existente.setEmail(dto.getEmail());
        Tecnico guardado = tecnicoRepository.save(existente);

        // Sincronizar actualización de email y/o contraseña en auth-service
        authServiceClient.actualizarUsuario(emailOriginal, dto.getEmail(), dto.getPassword());

        return guardado;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Tecnico tecnico = obtenerPorId(id);

        // Desvincular solicitudes asociadas poniéndolas en null antes de eliminar el técnico
        List<Solicitud> solicitudesAsignadas = solicitudRepository.findByTecnicoAsignadoId(id);
        for (Solicitud s : solicitudesAsignadas) {
            s.setTecnicoAsignado(null);
            solicitudRepository.save(s);
        }
        tecnicoRepository.deleteById(id);

        // Eliminar también su cuenta de acceso en el auth-service
        authServiceClient.eliminarUsuario(tecnico.getEmail());
    }

    @Override
    public List<Tecnico> buscarPorFiltros(String especialidad, String nombre) {
        if (especialidad != null && !especialidad.trim().isEmpty()) {
            return tecnicoRepository.findByEspecialidadIgnoreCase(especialidad);
        }
        if (nombre != null && !nombre.trim().isEmpty()) {
            return tecnicoRepository.findByNombreContainingIgnoreCase(nombre);
        }
        return tecnicoRepository.findAll();
    }
}
