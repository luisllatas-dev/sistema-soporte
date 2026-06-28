package com.sistema.solicitudes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.solicitudes.model.Tecnico;

/**
 * Repositorio JPA para la entidad Tecnico.
 * Spring Data JPA genera la implementación automáticamente.
 */
public interface ITecnicoRepository extends JpaRepository<Tecnico, Long> {
}
