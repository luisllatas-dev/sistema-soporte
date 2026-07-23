package com.sistema.solicitudes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistema.solicitudes.dto.ApiResponse;
import com.sistema.solicitudes.dto.ClienteRequestDTO;
import com.sistema.solicitudes.model.Cliente;
import com.sistema.solicitudes.service.interfaces.IClienteService;

import jakarta.validation.Valid;
    
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Obtiene todos los clientes.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Cliente>>> obtenerTodos() {
        List<Cliente> lista = clienteService.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.success(lista, "Lista de clientes obtenida exitosamente"));
    }

    /**
     * Obtiene un cliente por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(cliente, "Cliente encontrado exitosamente"));
    }

    /**
     * Registra un nuevo cliente.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Cliente>> crear(@Valid @RequestBody ClienteRequestDTO dto) {
        Cliente nuevo = clienteService.crear(dto);
        return new ResponseEntity<>(ApiResponse.success(nuevo, "Cliente creado exitosamente"), HttpStatus.CREATED);
    }

    /**
     * Actualiza la información de un cliente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        Cliente actualizado = clienteService.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(actualizado, "Cliente actualizado exitosamente"));
    }

    /**
     * Elimina un cliente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Cliente eliminado exitosamente"));
    }
}
