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
import com.sistema.solicitudes.dto.TecnicoRequestDTO;
import com.sistema.solicitudes.model.Tecnico;
import com.sistema.solicitudes.service.interfaces.ITecnicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tecnicos")
@Tag(name = "Técnicos", description = "API para la gestión de técnicos")
public class TecnicoController {

    private final ITecnicoService tecnicoService;

    public TecnicoController(ITecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    /**
     * Obtiene todos los técnicos o filtra por especialidad o nombre.
     */
    @Operation(summary = "Obtener técnicos", description = "Retorna la lista de técnicos o filtra por especialidad o coincidencia de nombre")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Tecnico>>> obtenerTodos(
            @RequestParam(required = false) String especialidad,
            @RequestParam(required = false) String nombre) {
        List<Tecnico> lista = tecnicoService.buscarPorFiltros(especialidad, nombre);
        return ResponseEntity.ok(ApiResponse.success(lista, "Lista de técnicos obtenida exitosamente"));
    }

    /**
     * Obtiene un técnico por su ID.
     */
    @Operation(summary = "Buscar técnico por ID", description = "Retorna un técnico por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Tecnico>> obtenerPorId(@PathVariable Long id) {
        Tecnico tecnico = tecnicoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(tecnico, "Técnico encontrado exitosamente"));
    }

    /**
     * Registra un nuevo técnico.
     */
    @Operation(summary = "Registrar técnico", description = "Crea un nuevo técnico en el sistema")
    @PostMapping
    public ResponseEntity<ApiResponse<Tecnico>> crear(@Valid @RequestBody TecnicoRequestDTO dto) {
        Tecnico nuevo = tecnicoService.crear(dto);
        return new ResponseEntity<>(ApiResponse.success(nuevo, "Técnico creado exitosamente"), HttpStatus.CREATED);
    }

    /**
     * Actualiza la información de un técnico.
     */
    @Operation(summary = "Actualizar técnico", description = "Actualiza un técnico existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Tecnico>> actualizar(@PathVariable Long id, @Valid @RequestBody TecnicoRequestDTO dto) {
        Tecnico actualizado = tecnicoService.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(actualizado, "Técnico actualizado exitosamente"));
    }

    /**
     * Elimina un técnico.
     */
    @Operation(summary = "Eliminar técnico", description = "Elimina un técnico por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        tecnicoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Técnico eliminado exitosamente"));
    }
}
