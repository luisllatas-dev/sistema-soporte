package com.sistema.solicitudes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistema.solicitudes.dto.ApiResponse;
import com.sistema.solicitudes.model.Tecnico;
import com.sistema.solicitudes.service.interfaces.ITecnicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController {

    private final ITecnicoService tecnicoService;

    public TecnicoController(ITecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    /**
     * Obtiene todos los técnicos.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Tecnico>>> obtenerTodos() {
        List<Tecnico> lista = tecnicoService.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.success(lista, "Lista de técnicos obtenida exitosamente"));
    }

    /**
     * Obtiene un técnico por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Tecnico>> obtenerPorId(@PathVariable Long id) {
        Tecnico tecnico = tecnicoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(tecnico, "Técnico encontrado exitosamente"));
    }

    /**
     * Registra un nuevo técnico.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Tecnico>> crear(@Valid @RequestBody Tecnico tecnico) {
        Tecnico nuevo = tecnicoService.crear(tecnico);
        return new ResponseEntity<>(ApiResponse.success(nuevo, "Técnico creado exitosamente"), HttpStatus.CREATED);
    }

    /**
     * Actualiza la información de un técnico.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Tecnico>> actualizar(@PathVariable Long id, @Valid @RequestBody Tecnico tecnico) {
        Tecnico actualizado = tecnicoService.actualizar(id, tecnico);
        return ResponseEntity.ok(ApiResponse.success(actualizado, "Técnico actualizado exitosamente"));
    }

    /**
     * Elimina un técnico.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        tecnicoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Técnico eliminado exitosamente"));
    }
}
