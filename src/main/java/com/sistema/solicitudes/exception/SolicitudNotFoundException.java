package com.sistema.solicitudes.exception;

/**
 * Excepción personalizada lanzada cuando no se encuentra una solicitud por su ID.
 */
public class SolicitudNotFoundException extends RuntimeException {

    public SolicitudNotFoundException(Long id) {
        super("No se encontró la solicitud con ID: " + id);
    }
}
