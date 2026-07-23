package com.sistema.solicitudes.service.interfaces;

import java.util.List;

import com.sistema.solicitudes.dto.TecnicoRequestDTO;
import com.sistema.solicitudes.model.Tecnico;

public interface ITecnicoService {

    List<Tecnico> obtenerTodos();

    Tecnico obtenerPorId(Long id);

    Tecnico crear(TecnicoRequestDTO dto);

    Tecnico actualizar(Long id, TecnicoRequestDTO dto);

    void eliminar(Long id);
}
