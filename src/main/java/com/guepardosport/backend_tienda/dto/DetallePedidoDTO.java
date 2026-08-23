package com.guepardosport.backend_tienda.dto;

import java.math.BigDecimal;

public class DetallePedidoDTO {
    private String prenda;
    private String color;
    private String talla;
    private Integer cantidad;
    private BigDecimal precioUnitario;

    public String getPrenda() { return prenda; }
    public void setPrenda(String prenda) { this.prenda = prenda; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    private BigDecimal ivaLinea;

    public BigDecimal getIvaLinea() { return ivaLinea; }
    public void setIvaLinea(BigDecimal ivaLinea) { this.ivaLinea = ivaLinea; }
}