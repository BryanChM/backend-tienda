package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.dto.RecurrenteWebhookDTO;
import com.guepardosport.backend_tienda.entity.Pedido;
import com.guepardosport.backend_tienda.repository.PedidoRepository;
import com.guepardosport.backend_tienda.service.EmailService;
import com.guepardosport.backend_tienda.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/recurrente")
public class RecurrenteWebhookController {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private EmailService emailService;
    @Autowired private FacturaService facturaService;

    @PostMapping
    public void recibir(@RequestBody RecurrenteWebhookDTO webhook) {
        if (!"checkout.completed".equals(webhook.getEvent()) && !"payment_intent.succeeded".equals(webhook.getEvent())) {
            return; // ignoramos eventos que no nos interesan
        }

        Map<String, Object> metadata = (Map<String, Object>) webhook.getData().get("metadata");
        if (metadata == null || !metadata.containsKey("pedido_id")) return;

        Long pedidoId = Long.valueOf(metadata.get("pedido_id").toString());

        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null) return;

        // Evita procesar el mismo pago dos veces si Recurrente reintenta el webhook
        if ("PAGADO".equals(pedido.getEstadoPago())) return;

        pedido.setEstadoPago("PAGADO");
        pedidoRepository.save(pedido);

        emailService.enviarConfirmacionPedido(pedido);

        var factura = facturaService.obtenerPorPedido(pedidoId);
        // el correo de factura se envía normalmente cuando el admin marca "pagado" manualmente;
        // para pago en línea, lo disparamos aquí automáticamente
    }
}