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
import com.sistema.solicitudes.dto.ClienteRequestDTO;
import com.sistema.solicitudes.model.Cliente;
import com.sistema.solicitudes.service.interfaces.IClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para la gestión de clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Obtiene todos los clientes u opcionalmente los filtra por nombre.
     */
    @Operation(summary = "Obtener clientes", description = "Retorna la lista de clientes o los filtra por coincidencia en el nombre")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Cliente>>> obtenerTodos(
            @RequestParam(required = false) String nombre) {
        List<Cliente> lista = clienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(ApiResponse.success(lista, "Lista de clientes obtenida exitosamente"));
    }

    /**
     * Obtiene un cliente por su ID.
     */
    @Operation(summary = "Buscar cliente por ID", description = "Retorna un cliente según su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(cliente, "Cliente encontrado exitosamente"));
    }

    /**
     * Registra un nuevo cliente.
     */
    @Operation(summary = "Registrar cliente", description = "Crea un nuevo cliente en el sistema")
    @PostMapping
    public ResponseEntity<ApiResponse<Cliente>> crear(@Valid @RequestBody ClienteRequestDTO dto) {
        Cliente nuevo = clienteService.crear(dto);
        return new ResponseEntity<>(ApiResponse.success(nuevo, "Cliente creado exitosamente"), HttpStatus.CREATED);
    }

    /**
     * Actualiza la información de un cliente.
     */
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        Cliente actualizado = clienteService.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(actualizado, "Cliente actualizado exitosamente"));
    }

    /**
     * Elimina un cliente.
     */
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Cliente eliminado exitosamente"));
    }
}
