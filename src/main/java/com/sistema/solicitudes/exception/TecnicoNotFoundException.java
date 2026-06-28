package com.sistema.solicitudes.exception;

/**
 * Excepción personalizada lanzada cuando no se encuentra un técnico por su ID.
 */
public class TecnicoNotFoundException extends RuntimeException {

    public TecnicoNotFoundException(Long id) {
        super("No se encontró el técnico con ID: " + id);
    }
}
