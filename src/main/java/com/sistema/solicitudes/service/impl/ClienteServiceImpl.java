package com.sistema.solicitudes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.solicitudes.exception.ClienteNotFoundException;
import com.sistema.solicitudes.model.Cliente;
import com.sistema.solicitudes.repository.IClienteRepository;
import com.sistema.solicitudes.service.interfaces.IClienteService;

/**
 * Implementación del servicio de clientes.
 * Delega la persistencia de datos a la capa de repositorio JPA.
 */
@Service
public class ClienteServiceImpl implements IClienteService {

    private final IClienteRepository clienteRepository;

    /**
     * Constructor con inyección de dependencias del repositorio.
     */
    public ClienteServiceImpl(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }

    @Override
    public Cliente crear(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Long id, Cliente cliente) {
        Cliente existente = obtenerPorId(id); // Lanza ClienteNotFoundException si no existe
        cliente.setId(id);
        cliente.setFechaCreacion(existente.getFechaCreacion());
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException(id);
        }
        clienteRepository.deleteById(id);
    }
}
