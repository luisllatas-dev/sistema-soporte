package com.sistema.solicitudes.service.interfaces;

import java.util.List;

import com.sistema.solicitudes.model.Tecnico;

public interface ITecnicoService {

    List<Tecnico> obtenerTodos();

    Tecnico obtenerPorId(Long id);

    Tecnico crear(Tecnico tecnico);

    Tecnico actualizar(Long id, Tecnico tecnico);

    void eliminar(Long id);
}
