package com.guepardosport.backend_tienda.service;

import com.guepardosport.backend_tienda.dto.ClienteAdminDTO;
import com.guepardosport.backend_tienda.entity.Cliente;
import com.guepardosport.backend_tienda.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteAdminService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteAdminDTO> listarTodos() {
        return clienteRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public ClienteAdminDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id " + id));
        return convertirADTO(cliente);
    }

    public ClienteAdminDTO actualizar(Long id, ClienteAdminDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id " + id));
        cliente.setNombre(dto.getNombre());
        cliente.setCorreo(dto.getCorreo());
        // Nota: la contraseña NO se actualiza aquí a propósito.
        // Si en el futuro se necesita, debe ir en un endpoint aparte con su propio flujo de verificación.
        return convertirADTO(clienteRepository.save(cliente));
    }

    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con id " + id);
        }
        clienteRepository.deleteById(id);
    }

    private ClienteAdminDTO convertirADTO(Cliente cliente) {
        ClienteAdminDTO dto = new ClienteAdminDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setCorreo(cliente.getCorreo());
        return dto;
    }
}