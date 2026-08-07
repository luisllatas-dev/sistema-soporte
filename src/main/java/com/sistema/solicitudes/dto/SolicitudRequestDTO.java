package com.sistema.solicitudes.dto;

import com.sistema.solicitudes.model.EstadoSolicitud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para crear o actualizar una solicitud de soporte técnico.
 * Recibe los IDs de cliente y técnico ya existentes en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudRequestDTO {

    @NotBlank(message = "La descripción de la solicitud es obligatoria")
    private String descripcion;

    @NotNull(message = "El estado de la solicitud es obligatorio")
    private EstadoSolicitud estado;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El ID del técnico asignado es obligatorio")
    private Long tecnicoAsignadoId;

    private String observaciones;
}
