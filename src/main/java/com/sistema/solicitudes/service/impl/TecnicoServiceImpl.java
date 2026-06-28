package com.sistema.solicitudes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.solicitudes.exception.TecnicoNotFoundException;
import com.sistema.solicitudes.model.Tecnico;
import com.sistema.solicitudes.repository.ITecnicoRepository;
import com.sistema.solicitudes.service.interfaces.ITecnicoService;

/**
 * Implementación del servicio de técnicos.
 * Delega la persistencia de datos a la capa de repositorio JPA.
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
    public Tecnico crear(Tecnico tecnico) {
        return tecnicoRepository.save(tecnico);
    }

    @Override
    public Tecnico actualizar(Long id, Tecnico tecnico) {
        Tecnico existente = obtenerPorId(id); // Lanza TecnicoNotFoundException si no existe
        tecnico.setId(id);
        tecnico.setFechaCreacion(existente.getFechaCreacion());
        return tecnicoRepository.save(tecnico);
    }

    @Override
    public void eliminar(Long id) {
        if (!tecnicoRepository.existsById(id)) {
            throw new TecnicoNotFoundException(id);
        }
        tecnicoRepository.deleteById(id);
    }
}
