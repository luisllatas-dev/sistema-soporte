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
            @RequestParam(required = false) EstadoSolicitud estado,
            org.springframework.security.core.Authentication auth) {
        
        List<Solicitud> lista;
        String email = (auth != null) ? auth.getName() : null;
        boolean esCliente = (auth != null) && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
        boolean esTecnico = (auth != null) && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TECNICO"));

        if (esCliente && email != null) {
            lista = solicitudService.obtenerMisSolicitudes(email);
            if (estado != null) {
                lista = lista.stream().filter(s -> s.getEstado() == estado).toList();
            }
        } else if (esTecnico && email != null) {
            lista = solicitudService.obtenerMisAsignaciones(email);
            if (estado != null) {
                lista = lista.stream().filter(s -> s.getEstado() == estado).toList();
            }
        } else {
            lista = solicitudService.obtenerTodas(estado);
        }

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
    public ResponseEntity<ApiResponse<Solicitud>> obtenerPorId(
            @PathVariable Long id,
            org.springframework.security.core.Authentication auth) {
        Solicitud solicitud = solicitudService.obtenerPorId(id);

        if (auth != null) {
            String email = auth.getName();
            boolean esCliente = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
            boolean esTecnico = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TECNICO"));

            if (esCliente && (solicitud.getCliente() == null || !solicitud.getCliente().getCorreoElectronico().equalsIgnoreCase(email))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Acceso denegado: No puede consultar solicitudes de otros clientes"));
            }

            if (esTecnico && (solicitud.getTecnicoAsignado() == null || !solicitud.getTecnicoAsignado().getEmail().equalsIgnoreCase(email))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Acceso denegado: No puede consultar solicitudes no asignadas a su cuenta"));
            }
        }

        return ResponseEntity.ok(ApiResponse.success(solicitud, "Solicitud encontrada exitosamente"));
    }

    /**
     * Obtiene las solicitudes de un cliente por su ID.
     */
    @Operation(summary = "Buscar solicitudes por Cliente", description = "Retorna el historial de solicitudes de un cliente específico")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<Solicitud>>> obtenerPorCliente(@PathVariable Long clienteId) {
        List<Solicitud> lista = solicitudService.obtenerPorCliente(clienteId);
        return ResponseEntity.ok(ApiResponse.success(lista, "Solicitudes del cliente obtenidas exitosamente"));
    }

    /**
     * Obtiene las solicitudes asignadas a un técnico por su ID.
     */
    @Operation(summary = "Buscar solicitudes por Técnico", description = "Retorna las solicitudes asignadas a un técnico específico")
    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<ApiResponse<List<Solicitud>>> obtenerPorTecnico(@PathVariable Long tecnicoId) {
        List<Solicitud> lista = solicitudService.obtenerPorTecnico(tecnicoId);
        return ResponseEntity.ok(ApiResponse.success(lista, "Solicitudes del técnico obtenidas exitosamente"));
    }

    /**
     * Busca solicitudes por coincidencias en la descripción.
     */
    @Operation(summary = "Buscar solicitudes por descripción", description = "Busca solicitudes conteniendo un texto específico en su descripción")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Solicitud>>> buscarPorDescripcion(@RequestParam String texto) {
        List<Solicitud> lista = solicitudService.buscarPorDescripcion(texto);
        return ResponseEntity.ok(ApiResponse.success(lista, "Búsqueda realizada exitosamente"));
    }

    /**
     * Endpoint exclusivo para que el CLIENTE consulte únicamente sus propias solicitudes.
     * Extrae el email del usuario autenticado en el token JWT.
     */
    @Operation(summary = "Ver mis solicitudes (Cliente)", description = "Retorna las solicitudes pertenecientes al cliente autenticado vía JWT")
    @GetMapping("/mis-solicitudes")
    public ResponseEntity<ApiResponse<List<Solicitud>>> obtenerMisSolicitudes(org.springframework.security.core.Authentication auth) {
        String email = auth.getName();
        List<Solicitud> lista = solicitudService.obtenerMisSolicitudes(email);
        return ResponseEntity.ok(ApiResponse.success(lista, "Mis solicitudes obtenidas exitosamente"));
    }

    /**
     * Endpoint exclusivo para que el TÉCNICO consulte únicamente las solicitudes que le fueron asignadas.
     * Extrae el email del técnico autenticado en el token JWT.
     */
    @Operation(summary = "Ver mis asignaciones (Técnico)", description = "Retorna las solicitudes asignadas al técnico autenticado vía JWT")
    @GetMapping("/mis-asignaciones")
    public ResponseEntity<ApiResponse<List<Solicitud>>> obtenerMisAsignaciones(org.springframework.security.core.Authentication auth) {
        String email = auth.getName();
        List<Solicitud> lista = solicitudService.obtenerMisAsignaciones(email);
        return ResponseEntity.ok(ApiResponse.success(lista, "Mis asignaciones obtenidas exitosamente"));
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
    public ResponseEntity<ApiResponse<Solicitud>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudRequestDTO dto,
            org.springframework.security.core.Authentication auth) {
        
        Solicitud existente = solicitudService.obtenerPorId(id);

        if (auth != null) {
            String email = auth.getName();
            boolean esCliente = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
            boolean esTecnico = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TECNICO"));

            if (esCliente) {
                if (existente.getCliente() == null || !existente.getCliente().getCorreoElectronico().equalsIgnoreCase(email)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(ApiResponse.error("Acceso denegado: No puede modificar solicitudes de otros clientes"));
                }
                if (existente.getEstado() != EstadoSolicitud.ABIERTA) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.error("Acceso denegado: Solo puede modificar solicitudes cuando aún no han sido atendidas (estado ABIERTA)"));
                }
            }

            if (esTecnico && (existente.getTecnicoAsignado() == null || !existente.getTecnicoAsignado().getEmail().equalsIgnoreCase(email))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Acceso denegado: No puede modificar solicitudes no asignadas a su cuenta"));
            }
        }

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
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @PathVariable Long id,
            org.springframework.security.core.Authentication auth) {
        
        if (auth != null) {
            boolean esCliente = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
            if (esCliente) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Acceso denegado: Los clientes no tienen permiso para eliminar solicitudes"));
            }
        }

        solicitudService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Solicitud eliminada exitosamente"));
    }
}
