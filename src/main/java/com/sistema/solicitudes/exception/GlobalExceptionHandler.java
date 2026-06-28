package com.sistema.solicitudes.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sistema.solicitudes.dto.ApiResponse;

/**
 * Manejador global de excepciones para la API.
 * Centraliza el manejo de errores y devuelve respuestas HTTP consistentes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones cuando no se encuentra una solicitud.
     * Retorna HTTP 404 Not Found.
     */
    @ExceptionHandler(SolicitudNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleSolicitudNotFound(SolicitudNotFoundException ex) {
        ApiResponse<Object> error = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones cuando no se encuentra un cliente.
     * Retorna HTTP 404 Not Found.
     */
    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleClienteNotFound(ClienteNotFoundException ex) {
        ApiResponse<Object> error = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones cuando no se encuentra un técnico.
     * Retorna HTTP 404 Not Found.
     */
    @ExceptionHandler(TecnicoNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleTecnicoNotFound(TecnicoNotFoundException ex) {
        ApiResponse<Object> error = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja errores de validación de los campos de entrada.
     * Retorna HTTP 400 Bad Request con detalle de cada campo inválido.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                details.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        ApiResponse<Object> error = ApiResponse.error("Los datos enviados contienen errores", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja cualquier excepción no controlada.
     * Retorna HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        ApiResponse<Object> error = ApiResponse.error("Ocurrió un error inesperado: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

