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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.solicitudes.dto.ApiResponse;
import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;
import com.sistema.solicitudes.service.interfaces.ISolicitudService;


import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de solicitudes de soporte técnico.
 * Expone los endpoints CRUD de la API envolviendo las respuestas en
 * ApiResponse.
 */
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final ISolicitudService solicitudService;

    public SolicitudController(ISolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    /**
     * Obtiene todas las solicitudes de soporte registradas.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Solicitud>>> obtenerTodas(
            @RequestParam(required = false) EstadoSolicitud estado) {
        List<Solicitud> lista = solicitudService.obtenerTodas(estado);
        return ResponseEntity.ok(ApiResponse.success(lista, "Lista de solicitudes obtenida exitosamente"));
    }

    /**
     * Obtiene una solicitud específica por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Solicitud>> obtenerPorId(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(solicitud, "Solicitud encontrada exitosamente"));
    }

    /**
     * Registra una nueva solicitud de soporte técnico.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Solicitud>> crear(@Valid @RequestBody Solicitud solicitud) {
        Solicitud nueva = solicitudService.crear(solicitud);
        return new ResponseEntity<>(ApiResponse.success(nueva, "Solicitud creada exitosamente"), HttpStatus.CREATED);
    }

    /**
     * Actualiza la información de una solicitud existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Solicitud>> actualizar(@PathVariable Long id,
            @Valid @RequestBody Solicitud solicitud) {
        Solicitud actualizada = solicitudService.actualizar(id, solicitud);
        return ResponseEntity.ok(ApiResponse.success(actualizada, "Solicitud actualizada exitosamente"));
    }

    /**
     * Actualiza únicamente el estado de una solicitud existente.
     */
    @PutMapping("/{id}/estado/{estado}")
    public ResponseEntity<ApiResponse<Solicitud>> actualizarEstado(
            @PathVariable Long id,
            @PathVariable EstadoSolicitud estado) {
        Solicitud actualizada = solicitudService.actualizarEstado(id, estado);
        return ResponseEntity.ok(ApiResponse.success(actualizada, "Estado de la solicitud actualizado exitosamente"));
    }

    /**
     * Elimina una solicitud de soporte técnico.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        solicitudService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Solicitud eliminada exitosamente"));
    }
}
