package com.sistema.solicitudes.exception;

/**
 * Excepción personalizada lanzada cuando no se encuentra un cliente por su ID.
 */
public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(Long id) {
        super("No se encontró el cliente con ID: " + id);
    }
}
