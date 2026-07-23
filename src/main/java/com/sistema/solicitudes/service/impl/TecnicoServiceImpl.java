package com.sistema.solicitudes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.solicitudes.dto.TecnicoRequestDTO;
import com.sistema.solicitudes.exception.TecnicoNotFoundException;
import com.sistema.solicitudes.model.Tecnico;
import com.sistema.solicitudes.repository.ITecnicoRepository;
import com.sistema.solicitudes.service.interfaces.ITecnicoService;

/**
 * Implementación del servicio de técnicos.
 */
@Service
public class TecnicoServiceImpl implements ITecnicoService {

    private final ITecnicoRepository tecnicoRepository;

    public TecnicoServiceImpl(ITecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
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
        return tecnicoRepository.save(tecnico);
    }

    @Override
    public Tecnico actualizar(Long id, TecnicoRequestDTO dto) {
        Tecnico existente = obtenerPorId(id);
        existente.setNombre(dto.getNombre());
        existente.setEspecialidad(dto.getEspecialidad());
        return tecnicoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        if (!tecnicoRepository.existsById(id)) {
            throw new TecnicoNotFoundException(id);
        }
        tecnicoRepository.deleteById(id);
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
