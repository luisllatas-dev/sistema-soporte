package com.sistema.solicitudes.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.sistema.solicitudes.exception.SolicitudNotFoundException;
import com.sistema.solicitudes.model.Cliente;
import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;
import com.sistema.solicitudes.model.Tecnico;
import com.sistema.solicitudes.service.interfaces.ISolicitudService;

/**
 * Implementación del servicio de solicitudes con persistencia en memoria.
 * Utiliza un Map para almacenar las solicitudes y un AtomicLong para generar IDs.
 */
@Service
public class SolicitudServiceImpl implements ISolicitudService {

    private final Map<Long, Solicitud> solicitudes = new LinkedHashMap<>();
    private final AtomicLong secuenciaId = new AtomicLong(1);

    /**
     * Constructor que precarga datos de ejemplo para demostración.
     */
    public SolicitudServiceImpl() {
        // Cliente 1
        Cliente cliente1 = new Cliente(1L, "Carlos Mendoza", "carlos.mendoza@email.com");
        Tecnico tecnico1 = new Tecnico(1L, "Ana García", "Redes y Conectividad");
        Solicitud solicitud1 = new Solicitud(
                secuenciaId.getAndIncrement(),
                "No hay conexión a internet en el área de contabilidad",
                EstadoSolicitud.ABIERTA,
                LocalDateTime.now(),
                null,
                cliente1,
                tecnico1
        );
        solicitudes.put(solicitud1.getId(), solicitud1);

        // Cliente 2
        Cliente cliente2 = new Cliente(2L, "María López", "maria.lopez@email.com");
        Tecnico tecnico2 = new Tecnico(2L, "Pedro Ruiz", "Hardware");
        Solicitud solicitud2 = new Solicitud(
                secuenciaId.getAndIncrement(),
                "La impresora del piso 3 no imprime correctamente",
                EstadoSolicitud.EN_PROCESO,
                LocalDateTime.now().minusDays(2),
                null,
                cliente2,
                tecnico2
        );
        solicitudes.put(solicitud2.getId(), solicitud2);

        // Cliente 3
        Cliente cliente3 = new Cliente(3L, "Jorge Castillo", "jorge.castillo@email.com");
        Solicitud solicitud3 = new Solicitud(
                secuenciaId.getAndIncrement(),
                "Actualización del sistema operativo en los equipos de ventas",
                EstadoSolicitud.CERRADA,
                LocalDateTime.now().minusDays(5),
                null,
                cliente3,
                tecnico1
        );
        solicitudes.put(solicitud3.getId(), solicitud3);
    }

    @Override
    public List<Solicitud> obtenerTodas() {
        return new ArrayList<>(solicitudes.values());
    }

    @Override
    public Solicitud obtenerPorId(Long id) {
        Solicitud solicitud = solicitudes.get(id);
        if (solicitud == null) {
            throw new SolicitudNotFoundException(id);
        }
        return solicitud;
    }

    @Override
    public Solicitud crear(Solicitud solicitud) {
        Long nuevoId = secuenciaId.getAndIncrement();
        solicitud.setId(nuevoId);
        solicitud.setFechaCreacion(LocalDateTime.now());

        // Asignar IDs a cliente y técnico si no los tienen
        if (solicitud.getCliente().getId() == null) {
            solicitud.getCliente().setId(nuevoId * 100 + 1);
        }
        if (solicitud.getTecnicoAsignado().getId() == null) {
            solicitud.getTecnicoAsignado().setId(nuevoId * 100 + 2);
        }

        solicitudes.put(nuevoId, solicitud);
        return solicitud;
    }

    @Override
    public Solicitud actualizar(Long id, Solicitud solicitud) {
        if (!solicitudes.containsKey(id)) {
            throw new SolicitudNotFoundException(id);
        }
        solicitud.setId(id);

        // Mantener la fecha de creación original
        Solicitud existente = solicitudes.get(id);
        solicitud.setFechaCreacion(existente.getFechaCreacion());
        solicitud.setFechaActualizacion(LocalDateTime.now());

        solicitudes.put(id, solicitud);
        return solicitud;
    }

    @Override
    public void eliminar(Long id) {
        if (!solicitudes.containsKey(id)) {
            throw new SolicitudNotFoundException(id);
        }
        solicitudes.remove(id);
    }
}
