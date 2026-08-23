package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.dto.FacturaDTO;
import com.guepardosport.backend_tienda.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/facturas")
@CrossOrigin(origins = "http://localhost:4200")
public class FacturaAdminController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public List<FacturaDTO> listar() {
        return facturaService.listarTodas();
    }

    @GetMapping("/{id}")
    public FacturaDTO obtener(@PathVariable Long id) {
        return facturaService.obtenerPorId(id);
    }

    @GetMapping("/por-pedido/{pedidoId}")
    public FacturaDTO obtenerPorPedido(@PathVariable Long pedidoId) {
        return facturaService.obtenerPorPedido(pedidoId);
    }

    // Botón para reintentar certificación cuando ya esté activo el proveedor FEL real
    @PostMapping("/{id}/reintentar-certificacion")
    public void reintentarCertificacion(@PathVariable Long id) {
        var factura = facturaService.obtenerPorId(id); // valida que exista
        // (En una versión más completa, cargaríamos la entidad real, no el DTO, para reintentar)
    }
}