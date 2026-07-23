package com.sistema.solicitudes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.solicitudes.model.Tecnico;

/**
 * Repositorio JPA para la entidad Tecnico.
 * Spring Data JPA genera la implementación automáticamente.
 */
public interface ITecnicoRepository extends JpaRepository<Tecnico, Long> {

    /**
     * Busca técnicos por especialidad (case insensitive).
     */
    List<Tecnico> findByEspecialidadIgnoreCase(String especialidad);

    /**
     * Busca técnicos por nombre que contenga el texto proporcionado (case insensitive).
     */
    List<Tecnico> findByNombreContainingIgnoreCase(String nombre);
}
