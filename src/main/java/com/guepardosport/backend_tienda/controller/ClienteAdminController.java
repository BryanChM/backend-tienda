package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.dto.ClienteAdminDTO;
import com.guepardosport.backend_tienda.service.ClienteAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteAdminController {

    @Autowired
    private ClienteAdminService clienteAdminService;

    @GetMapping
    public List<ClienteAdminDTO> listar() {
        return clienteAdminService.listarTodos();
    }

    @GetMapping("/{id}")
    public ClienteAdminDTO obtener(@PathVariable Long id) {
        return clienteAdminService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public ClienteAdminDTO actualizar(@PathVariable Long id, @RequestBody ClienteAdminDTO dto) {
        return clienteAdminService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteAdminService.eliminar(id);
    }
}