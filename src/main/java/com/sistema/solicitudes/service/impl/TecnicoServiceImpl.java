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
 * Recibe DTOs de entrada y los convierte a entidades JPA para persistencia.
 */
@Service
public class TecnicoServiceImpl implements ITecnicoService {

    private final ITecnicoRepository tecnicoRepository;

    /**
     * Constructor con inyección de dependencias del repositorio.
     */
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
}
