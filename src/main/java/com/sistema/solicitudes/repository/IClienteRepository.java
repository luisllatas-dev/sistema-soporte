package com.sistema.solicitudes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.solicitudes.model.Cliente;

/**
 * Repositorio JPA para la entidad Cliente.
 * Spring Data JPA genera la implementación automáticamente.
 */
public interface IClienteRepository extends JpaRepository<Cliente, Long> {
}
