package com.sistema.solicitudes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.solicitudes.model.Cliente;

/**
 * Repositorio JPA para la entidad Cliente.
 * Spring Data JPA genera la implementación automáticamente.
 */
public interface IClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByCorreoElectronico(String correoElectronico);

    /**
     * Busca clientes por nombre que contenga el texto proporcionado (case insensitive).
     */
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}
