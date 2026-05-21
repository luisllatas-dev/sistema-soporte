package com.sistema.solicitudes.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo que representa a un técnico asignado a una solicitud de soporte.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tecnico {

    private Long id;

    @NotBlank(message = "El nombre del técnico es obligatorio")
    private String nombre;

    @NotBlank(message = "La especialidad del técnico es obligatoria")
    private String especialidad;
}
