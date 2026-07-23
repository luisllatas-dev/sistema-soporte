package com.sistema.solicitudes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.solicitudes.dto.ApiResponse;
import com.sistema.solicitudes.dto.SolicitudRequestDTO;
import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;
import com.sistema.solicitudes.service.interfaces.ISolicitudService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de solicitudes de soporte técnico.
 * Expone los endpoints CRUD de la API envolviendo las respuestas en ApiResponse.
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<Solicitud>>> obtenerTodas(
            @RequestParam(required = false) EstadoSolicitud estado) {
        List<Solicitud> lista = solicitudService.obtenerTodas(estado);
        return ResponseEntity.ok(ApiResponse.success(lista, "Lista de solicitudes obtenida exitosamente"));
    }

    /**
     * Obtiene una solicitud específica por su ID.
     */
    @Operation(summary = "Buscar solicitud por ID",
               description = "Retorna una solicitud de soporte técnico según su identificador único")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Solicitud encontrada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Solicitud>> obtenerPorId(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(solicitud, "Solicitud encontrada exitosamente"));
    }

    /**
     * Registra una nueva solicitud de soporte técnico.
     */
    @Operation(summary = "Registrar nueva solicitud",
               description = "Crea y registra una nueva solicitud de soporte técnico referenciando cliente y técnico por ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente o técnico no encontrado")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Solicitud>> crear(@Valid @RequestBody SolicitudRequestDTO dto) {
        Solicitud nueva = solicitudService.crear(dto);
        return new ResponseEntity<>(ApiResponse.success(nueva, "Solicitud creada exitosamente"), HttpStatus.CREATED);
    }

    /**
     * Actualiza la información de una solicitud existente.
     */
    @Operation(summary = "Actualizar solicitud",
               description = "Actualiza los datos de una solicitud de soporte técnico existente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Solicitud actualizada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Solicitud, cliente o técnico no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Solicitud>> actualizar(@PathVariable Long id,
                                                             @Valid @RequestBody SolicitudRequestDTO dto) {
        Solicitud actualizada = solicitudService.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(actualizada, "Solicitud actualizada exitosamente"));
    }

    /**
     * Actualiza únicamente el estado de una solicitud existente.
     */
    @Operation(summary = "Actualizar estado de una solicitud",
               description = "Actualiza el estado de una solicitud de soporte técnico según su ID y el nuevo estado especificado")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @PatchMapping("/{id}/estado/{estado}")
    public ResponseEntity<ApiResponse<Solicitud>> actualizarEstado(
            @PathVariable Long id,
            @PathVariable EstadoSolicitud estado) {
        Solicitud actualizada = solicitudService.actualizarEstado(id, estado);
        return ResponseEntity.ok(ApiResponse.success(actualizada, "Estado de la solicitud actualizado exitosamente"));
    }

    /**
     * Elimina una solicitud de soporte técnico.
     */
    @Operation(summary = "Eliminar solicitud",
               description = "Elimina una solicitud de soporte técnico del sistema según su ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Solicitud eliminada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        solicitudService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Solicitud eliminada exitosamente"));
    }
}
