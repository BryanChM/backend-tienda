package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.dto.*;
import com.guepardosport.backend_tienda.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Checkout: público, permite invitado o registrado (CU-04, CU-06)
    @PostMapping("/api/pedidos")
    public PedidoResponseDTO crear(@Valid @RequestBody CheckoutRequestDTO dto) {
        return pedidoService.crearPedido(dto);
    }

    // Cliente consulta su pedido por id (CU-07, CU-14)
    @GetMapping("/api/pedidos/{id}")
    public PedidoResponseDTO obtener(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id);
    }

    // Cliente cancela su pedido (CU-15)
    @PutMapping("/api/pedidos/{id}/cancelar")
    public PedidoResponseDTO cancelar(@PathVariable Long id) {
        return pedidoService.cancelar(id);
    }

    // --- Rutas de administración (CU-11, CU-12) ---

    @GetMapping("/api/admin/pedidos")
    public List<PedidoResponseDTO> listar() {
        return pedidoService.listarTodos();
    }

    @PutMapping("/api/admin/pedidos/{id}/estado-logistico")
    public PedidoResponseDTO actualizarEstado(@PathVariable Long id, @RequestBody ActualizarEstadoDTO dto) {
        return pedidoService.actualizarEstadoLogistico(id, dto.getValor());
    }

    @PutMapping("/api/admin/pedidos/{id}/marcar-pagado")
    public PedidoResponseDTO marcarPagado(@PathVariable Long id) {
        return pedidoService.marcarComoPagado(id);
    }
    @PutMapping("/api/admin/pedidos/{id}/rastreo")
    public PedidoResponseDTO actualizarRastreo(@PathVariable Long id, @RequestBody ActualizarRastreoDTO dto) {
        return pedidoService.actualizarRastreo(id, dto);
    }

}