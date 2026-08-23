package com.guepardosport.backend_tienda.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "prenda_imagen")
public class PrendaImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prenda_color_id", nullable = false)
    private PrendaColor prendaColor;

    @Column(name = "url_imagen", nullable = false, length = 500)
    private String urlImagen;

    private Integer orden = 0;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    public PrendaImagen() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PrendaColor getPrendaColor() { return prendaColor; }
    public void setPrendaColor(PrendaColor prendaColor) { this.prendaColor = prendaColor; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public Boolean getEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(Boolean esPrincipal) { this.esPrincipal = esPrincipal; }
}