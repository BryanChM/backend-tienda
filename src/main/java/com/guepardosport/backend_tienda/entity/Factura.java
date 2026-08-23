package com.guepardosport.backend_tienda.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    // Correlativo interno mientras no hay FEL real (ej: "GS-000001")
    @Column(name = "numero_factura", nullable = false, unique = true, length = 30)
    private String numeroFactura;

    @Column(nullable = false, length = 20)
    private String nit; // "CF" si es consumidor final sin NIT

    @Column(name = "nombre_facturacion", nullable = false, length = 150)
    private String nombreFacturacion;

    @Column(name = "direccion_fiscal", length = 255)
    private String direccionFiscal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal iva;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    // PENDIENTE_CERTIFICACION / CERTIFICADA / ANULADA / ERROR_CERTIFICACION
    @Column(name = "estado_fel", nullable = false, length = 30)
    private String estadoFel = "PENDIENTE_CERTIFICACION";

    // Datos que llenará el proveedor certificador cuando esté activo (nulos por ahora)
    @Column(name = "uuid_fel", length = 100)
    private String uuidFel;

    @Column(name = "serie_fel", length = 30)
    private String serieFel;

    @Column(name = "fecha_certificacion")
    private LocalDateTime fechaCertificacion;

    public Factura() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

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

    public LocalDateTime getFechaCertificacion() { return fechaCertificacion; }
    public void setFechaCertificacion(LocalDateTime fechaCertificacion) { this.fechaCertificacion = fechaCertificacion; }
}