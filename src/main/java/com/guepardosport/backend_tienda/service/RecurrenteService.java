package com.guepardosport.backend_tienda.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class RecurrenteService {

    @Value("${recurrente.secret-key}") private String secretKey;
    @Value("${app.frontend-url}") private String frontendUrl;

    private static final String API_URL = "https://app.recurrente.com/api/checkouts";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Crea un checkout hospedado en Recurrente y devuelve la URL a la que
     * hay que redirigir al cliente para que pague con su tarjeta.
     */
    public String crearCheckout(Long pedidoId, BigDecimal total, String nombreItem) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-SECRET-KEY", secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        long montoEnCentavos = total.multiply(BigDecimal.valueOf(100)).longValueExact();

        Map<String, Object> item = Map.of(
                "name", nombreItem,
                "amount_in_cents", montoEnCentavos,
                "currency", "GTQ"
        );

        Map<String, Object> body = Map.of(
                "items", List.of(item),
                "success_url", frontendUrl + "/checkout/exito?pedido=" + pedidoId,
                "cancel_url", frontendUrl + "/checkout/cancelado?pedido=" + pedidoId,
                "metadata", Map.of("pedido_id", pedidoId.toString())
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);

        if (response.getBody() == null || !response.getBody().containsKey("checkout_url")) {
            throw new RuntimeException("No se pudo generar el enlace de pago");
        }

        return (String) response.getBody().get("checkout_url");
    }
}