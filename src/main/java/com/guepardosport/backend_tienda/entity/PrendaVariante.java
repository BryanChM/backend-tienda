package com.guepardosport.backend_tienda.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "prenda_variante")
public class PrendaVariante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prenda_color_id", nullable = false)
    private PrendaColor prendaColor;

    @Column(nullable = false, length = 10)
    private String talla;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(unique = true, length = 50)
    private String sku;
    @Version
    private Long version;

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public PrendaVariante() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PrendaColor getPrendaColor() { return prendaColor; }
    public void setPrendaColor(PrendaColor prendaColor) { this.prendaColor = prendaColor; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
}