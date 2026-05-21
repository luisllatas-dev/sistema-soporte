package com.sistema.solicitudes.model;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo principal que representa una solicitud de soporte técnico.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {

    private Long id;

    @NotBlank(message = "La descripción de la solicitud es obligatoria")
    private String descripcion;

    @NotNull(message = "El estado de la solicitud es obligatorio")
    private EstadoSolicitud estado;

    private LocalDateTime fechaCreacion;

    @NotNull(message = "El cliente es obligatorio")
    @Valid
    private Cliente cliente;

    @NotNull(message = "El técnico asignado es obligatorio")
    @Valid
    private Tecnico tecnicoAsignado;
}
