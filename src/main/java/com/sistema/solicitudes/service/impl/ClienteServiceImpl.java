package com.sistema.solicitudes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.solicitudes.client.AuthServiceClient;
import com.sistema.solicitudes.dto.ClienteRequestDTO;
import com.sistema.solicitudes.exception.ClienteNotFoundException;
import com.sistema.solicitudes.model.Cliente;
import com.sistema.solicitudes.repository.IClienteRepository;
import com.sistema.solicitudes.service.interfaces.IClienteService;

import org.springframework.transaction.annotation.Transactional;
import com.sistema.solicitudes.repository.ISolicitudRepository;

/**
 * Implementación del servicio de clientes.
 */
@Service
public class ClienteServiceImpl implements IClienteService {

    private final IClienteRepository clienteRepository;
    private final ISolicitudRepository solicitudRepository;
    private final AuthServiceClient authServiceClient;

    public ClienteServiceImpl(IClienteRepository clienteRepository, ISolicitudRepository solicitudRepository, AuthServiceClient authServiceClient) {
        this.clienteRepository = clienteRepository;
        this.solicitudRepository = solicitudRepository;
        this.authServiceClient = authServiceClient;
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
        Cliente guardado = clienteRepository.save(cliente);

        // Crear la cuenta de usuario en el auth-service con rol ROLE_CLIENTE
        String pass = (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) ? dto.getPassword() : "Cliente123#";
        authServiceClient.crearUsuario(dto.getCorreoElectronico(), pass, "ROLE_CLIENTE");

        return guardado;
    }

    @Override
    public Cliente actualizar(Long id, ClienteRequestDTO dto) {
        Cliente existente = obtenerPorId(id);
        String emailOriginal = existente.getCorreoElectronico();

        existente.setNombre(dto.getNombre());
        existente.setCorreoElectronico(dto.getCorreoElectronico());
        Cliente guardado = clienteRepository.save(existente);

        // Sincronizar actualización de email y/o contraseña en auth-service
        authServiceClient.actualizarUsuario(emailOriginal, dto.getCorreoElectronico(), dto.getPassword());

        return guardado;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);
        
        // Borrar primero las solicitudes asociadas a este cliente para evitar error de FK
        solicitudRepository.deleteByClienteId(id);
        clienteRepository.deleteById(id);

        // Eliminar también su cuenta de acceso en el auth-service
        authServiceClient.eliminarUsuario(cliente.getCorreoElectronico());
    }

    @Override
    public List<Cliente> buscarPorNombre(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return clienteRepository.findAll();
        }
        return clienteRepository.findByNombreContainingIgnoreCaseOrCorreoElectronicoContainingIgnoreCase(texto, texto);
    }
}
