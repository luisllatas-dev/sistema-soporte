package com.sistema.solicitudes.service.interfaces;

import java.util.List;

import com.sistema.solicitudes.dto.ClienteRequestDTO;
import com.sistema.solicitudes.model.Cliente;

public interface IClienteService {

    List<Cliente> obtenerTodos();

    Cliente obtenerPorId(Long id);

    Cliente crear(ClienteRequestDTO dto);

    Cliente actualizar(Long id, ClienteRequestDTO dto);

    void eliminar(Long id);
}
