package com.sistema.solicitudes.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.sistema.solicitudes.model.Cliente;
import com.sistema.solicitudes.model.EstadoSolicitud;
import com.sistema.solicitudes.model.Solicitud;
import com.sistema.solicitudes.model.Tecnico;

/**
 * Implementación del repositorio de solicitudes con persistencia simulada en memoria.
 */
@Repository
public class SolicitudRepositoryImpl implements ISolicitudRepository {

    private final Map<Long, Solicitud> solicitudes = new LinkedHashMap<>();
    private final AtomicLong secuenciaId = new AtomicLong(1);

    /**
     * Constructor que precarga los datos de ejemplo iniciales.
     */
    public SolicitudRepositoryImpl() {
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
    public Optional<Solicitud> obtenerPorId(Long id) {
        return Optional.ofNullable(solicitudes.get(id));
    }

    @Override
    public Solicitud guardar(Solicitud solicitud) {
        if (solicitud.getId() == null) {
            Long nuevoId = secuenciaId.getAndIncrement();
            solicitud.setId(nuevoId);
        }
        solicitudes.put(solicitud.getId(), solicitud);
        return solicitud;
    }

    @Override
    public void eliminar(Long id) {
        solicitudes.remove(id);
    }

    @Override
    public boolean existe(Long id) {
        return solicitudes.containsKey(id);
    }
}
