package com.sistema.solicitudes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.solicitudes.model.Solicitud;
import com.sistema.solicitudes.service.interfaces.ISolicitudService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de solicitudes de soporte técnico.
 * Expone los endpoints CRUD de la API.
 */
@RestController
@RequestMapping("/api/solicitudes")
@Tag(name = "Solicitudes de Soporte", description = "API para gestionar solicitudes de soporte técnico")
public class SolicitudController {

    private final ISolicitudService solicitudService;

    public SolicitudController(ISolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    /**
     * Obtiene todas las solicitudes de soporte registradas.
     */
    @Operation(summary = "Listar todas las solicitudes",
               description = "Retorna una lista con todas las solicitudes de soporte técnico registradas en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Solicitud>> obtenerTodas() {
        return ResponseEntity.ok(solicitudService.obtenerTodas());
    }

    /**
     * Obtiene una solicitud específica por su ID.
     */
    @Operation(summary = "Buscar solicitud por ID",
               description = "Retorna una solicitud de soporte técnico según su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.obtenerPorId(id));
    }

    /**
     * Registra una nueva solicitud de soporte técnico.
     */
    @Operation(summary = "Registrar nueva solicitud",
               description = "Crea y registra una nueva solicitud de soporte técnico en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<Solicitud> crear(@Valid @RequestBody Solicitud solicitud) {
        Solicitud nueva = solicitudService.crear(solicitud);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    /**
     * Actualiza la información de una solicitud existente.
     */
    @Operation(summary = "Actualizar solicitud",
               description = "Actualiza los datos de una solicitud de soporte técnico existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizar(@PathVariable Long id,
                                                 @Valid @RequestBody Solicitud solicitud) {
        return ResponseEntity.ok(solicitudService.actualizar(id, solicitud));
    }

    /**
     * Elimina una solicitud de soporte técnico.
     */
    @Operation(summary = "Eliminar solicitud",
               description = "Elimina una solicitud de soporte técnico del sistema según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Solicitud eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        solicitudService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
