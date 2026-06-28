package com.sistema.solicitudes.service.interfaces;

import java.util.List;

import com.sistema.solicitudes.model.Cliente;

public interface IClienteService {

    List<Cliente> obtenerTodos();

    Cliente obtenerPorId(Long id);

    Cliente crear(Cliente cliente);

    Cliente actualizar(Long id, Cliente cliente);

    void eliminar(Long id);
}
