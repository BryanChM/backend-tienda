package com.guepardosport.backend_tienda.dto;

import java.math.BigDecimal;
import java.util.List;

public class PrendaResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private String estado;
    private List<String> deportes;
    private List<PrendaColorDTO> colores;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecioBase() { return precioBase; }
    public void setPrecioBase(BigDecimal precioBase) { this.precioBase = precioBase; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<String> getDeportes() { return deportes; }
    public void setDeportes(List<String> deportes) { this.deportes = deportes; }

    public List<PrendaColorDTO> getColores() { return colores; }
    public void setColores(List<PrendaColorDTO> colores) { this.colores = colores; }
    private String genero;

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    private BigDecimal precioConIva;

    public BigDecimal getPrecioConIva() { return precioConIva; }
    public void setPrecioConIva(BigDecimal precioConIva) { this.precioConIva = precioConIva; }
}