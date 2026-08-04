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
     * Busca clientes por nombre o correo electrónico que contengan el texto (case insensitive).
     */
    List<Cliente> findByNombreContainingIgnoreCaseOrCorreoElectronicoContainingIgnoreCase(String nombre, String correo);
}
