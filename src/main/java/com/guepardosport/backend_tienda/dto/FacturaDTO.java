package com.guepardosport.backend_tienda.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FacturaDTO {
    private Long id;
    private Long pedidoId;
    private String numeroFactura;
    private String nit;
    private String nombreFacturacion;
    private String direccionFiscal;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;
    private LocalDateTime fechaEmision;
    private String estadoFel;
    private String uuidFel;
    private String serieFel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getNombreFacturacion() { return nombreFacturacion; }
    public void setNombreFacturacion(String nombreFacturacion) { this.nombreFacturacion = nombreFacturacion; }

    public String getDireccionFiscal() { return direccionFiscal; }
    public void setDireccionFiscal(String direccionFiscal) { this.direccionFiscal = direccionFiscal; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getEstadoFel() { return estadoFel; }
    public void setEstadoFel(String estadoFel) { this.estadoFel = estadoFel; }

    public String getUuidFel() { return uuidFel; }
    public void setUuidFel(String uuidFel) { this.uuidFel = uuidFel; }

    public String getSerieFel() { return serieFel; }
    public void setSerieFel(String serieFel) { this.serieFel = serieFel; }
}