package com.sistema.solicitudes.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico para estandarizar las respuestas de la API.
 * @param <T> Tipo de datos que contiene la respuesta en caso de éxito.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Object errors;
    private LocalDateTime timestamp;

    /**
     * Genera una respuesta exitosa con datos y mensaje personalizado.
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Genera una respuesta exitosa simple.
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operación realizada con éxito");
    }

    /**
     * Genera una respuesta de error con mensaje y detalles específicos de la falla.
     */
    public static <T> ApiResponse<T> error(String message, Object errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Genera una respuesta de error simple con mensaje.
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(message, null);
    }
}
