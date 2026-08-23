package com.guepardosport.backend_tienda.service;

import com.guepardosport.backend_tienda.dto.FacturaDTO;
import com.guepardosport.backend_tienda.entity.Factura;
import com.guepardosport.backend_tienda.entity.Pedido;
import com.guepardosport.backend_tienda.repository.FacturaRepository;
import com.guepardosport.backend_tienda.service.fel.FelCertificadorService;
import com.guepardosport.backend_tienda.service.fel.ResultadoCertificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    private static final String PREFIJO_SERIE = "GS-";

    @Autowired private FacturaRepository facturaRepository;
    @Autowired private FelCertificadorService felCertificadorService; // Spring inyecta el Mock por ahora

    // Se llama automáticamente al confirmar un pedido (CU-20)
    public Factura generarFactura(Pedido pedido, String nit, String nombreFacturacion, String direccionFiscal) {
        Factura factura = new Factura();
        factura.setPedido(pedido);
        factura.setNumeroFactura(generarCorrelativo());
        factura.setNit((nit == null || nit.isBlank()) ? "CF" : nit.trim().toUpperCase());
        factura.setNombreFacturacion(
                (nombreFacturacion == null || nombreFacturacion.isBlank())
                        ? pedido.getNombreContacto()
                        : nombreFacturacion
        );
        factura.setDireccionFiscal(direccionFiscal);
        factura.setSubtotal(pedido.getSubtotal().subtract(pedido.getDescuento()).add(pedido.getCostoEnvio()));
        factura.setIva(pedido.getIva());
        factura.setTotal(pedido.getTotal());

        Factura guardada = facturaRepository.save(factura);

        // Intenta certificar (hoy siempre devuelve "pendiente" con el Mock; en el futuro, certificará de verdad)
        intentarCertificar(guardada);

        return guardada;
    }

    public void intentarCertificar(Factura factura) {
        ResultadoCertificacion resultado = felCertificadorService.certificar(factura);

        if (resultado.isExitoso()) {
            factura.setEstadoFel("CERTIFICADA");
            factura.setUuidFel(resultado.getUuidFel());
            factura.setSerieFel(resultado.getSerieFel());
            factura.setFechaCertificacion(LocalDateTime.now());
        } else {
            factura.setEstadoFel("PENDIENTE_CERTIFICACION");
        }
        facturaRepository.save(factura);
    }

    public List<FacturaDTO> listarTodas() {
        return facturaRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public FacturaDTO obtenerPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con id " + id));
        return convertirADTO(factura);
    }

    public FacturaDTO obtenerPorPedido(Long pedidoId) {
        Factura factura = facturaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RuntimeException("No existe factura para el pedido " + pedidoId));
        return convertirADTO(factura);
    }

    // Correlativo simple tipo GS-000001, GS-000002, ...
    private String generarCorrelativo() {
        long total = facturaRepository.count() + 1;
        return PREFIJO_SERIE + String.format("%06d", total);
    }

    private FacturaDTO convertirADTO(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(factura.getId());
        dto.setPedidoId(factura.getPedido().getId());
        dto.setNumeroFactura(factura.getNumeroFactura());
        dto.setNit(factura.getNit());
        dto.setNombreFacturacion(factura.getNombreFacturacion());
        dto.setDireccionFiscal(factura.getDireccionFiscal());
        dto.setSubtotal(factura.getSubtotal());
        dto.setIva(factura.getIva());
        dto.setTotal(factura.getTotal());
        dto.setFechaEmision(factura.getFechaEmision());
        dto.setEstadoFel(factura.getEstadoFel());
        dto.setUuidFel(factura.getUuidFel());
        dto.setSerieFel(factura.getSerieFel());
        return dto;
    }
}