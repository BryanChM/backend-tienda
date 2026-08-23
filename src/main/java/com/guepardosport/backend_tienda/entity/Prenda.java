package com.guepardosport.backend_tienda.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "prenda")
public class Prenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBase;

    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO"; // ACTIVO / DESCONTINUADO

    public Prenda() {}

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
    @ManyToMany
    @JoinTable(
            name = "prenda_deporte",
            joinColumns = @JoinColumn(name = "prenda_id"),
            inverseJoinColumns = @JoinColumn(name = "deporte_id")
    )
    private java.util.Set<Deporte> deportes = new java.util.HashSet<>();

    public java.util.Set<Deporte> getDeportes() { return deportes; }
    public void setDeportes(java.util.Set<Deporte> deportes) { this.deportes = deportes; }
    @OneToMany(mappedBy = "prenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<PrendaColor> colores;

    public java.util.List<PrendaColor> getColores() { return colores; }
    public void setColores(java.util.List<PrendaColor> colores) { this.colores = colores; }
    @Column(nullable = false, length = 20)
    private String genero = "UNISEX"; // MASCULINO / FEMENINO / UNISEX

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}