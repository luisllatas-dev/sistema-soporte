package com.sistema.solicitudes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para crear o actualizar un técnico.
 * Incluye email y password para crear automáticamente la cuenta en el auth-service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoRequestDTO {

    @NotBlank(message = "El nombre del técnico es obligatorio")
    private String nombre;

    @NotBlank(message = "La especialidad del técnico es obligatoria")
    private String especialidad;

    @NotBlank(message = "El email del técnico es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    private String password;
}
