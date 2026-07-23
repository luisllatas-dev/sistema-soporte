package com.sistema.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para crear o actualizar un técnico.
 * Contiene las validaciones de Spring Validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoRequestDTO {

    @NotBlank(message = "El nombre del técnico es obligatorio")
    private String nombre;

    @NotBlank(message = "La especialidad del técnico es obligatoria")
    private String especialidad;
}
