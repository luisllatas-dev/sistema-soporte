package com.sistema.solicitudes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.solicitudes.dto.ClienteRequestDTO;
import com.sistema.solicitudes.exception.ClienteNotFoundException;
import com.sistema.solicitudes.model.Cliente;
import com.sistema.solicitudes.repository.IClienteRepository;
import com.sistema.solicitudes.service.interfaces.IClienteService;

/**
 * Implementación del servicio de clientes.
 */
@Service
public class ClienteServiceImpl implements IClienteService {

    private final IClienteRepository clienteRepository;

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
    public Cliente crear(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setCorreoElectronico(dto.getCorreoElectronico());
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Long id, ClienteRequestDTO dto) {
        Cliente existente = obtenerPorId(id);
        existente.setNombre(dto.getNombre());
        existente.setCorreoElectronico(dto.getCorreoElectronico());
        return clienteRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException(id);
        }
        clienteRepository.deleteById(id);
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return clienteRepository.findAll();
        }
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
