package com.guepardosport.backend_tienda.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "prenda_color")
public class PrendaColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prenda_id", nullable = false)
    private Prenda prenda;

    @Column(nullable = false, length = 40)
    private String color;

    @OneToMany(mappedBy = "prendaColor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrendaImagen> imagenes;

    @OneToMany(mappedBy = "prendaColor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrendaVariante> variantes;

    public PrendaColor() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Prenda getPrenda() { return prenda; }
    public void setPrenda(Prenda prenda) { this.prenda = prenda; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public List<PrendaImagen> getImagenes() { return imagenes; }
    public void setImagenes(List<PrendaImagen> imagenes) { this.imagenes = imagenes; }

    public List<PrendaVariante> getVariantes() { return variantes; }
    public void setVariantes(List<PrendaVariante> variantes) { this.variantes = variantes; }
}