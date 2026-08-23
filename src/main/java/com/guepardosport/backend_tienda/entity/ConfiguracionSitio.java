package com.guepardosport.backend_tienda.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "configuracion_sitio")
public class ConfiguracionSitio {

    @Id
    private Long id = 1L; // fila única, siempre id=1

    @Column(name = "titulo_hero", length = 100)
    private String tituloHero = "Explora tu deporte";

    @Column(name = "subtitulo_hero", length = 200)
    private String subtituloHero = "Ropa deportiva para quienes no bajan el ritmo.";

    @Column(name = "texto_banner", length = 300)
    private String textoBanner = "ENVÍO GRATIS EN COMPRAS +Q300 · CAMBIOS EN 30 DÍAS · PAGO CONTRA ENTREGA DISPONIBLE";

    @Column(name = "color_marca", length = 10)
    private String colorMarca = "#F2A21B";

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTituloHero() { return tituloHero; }
    public void setTituloHero(String v) { this.tituloHero = v; }

    public String getSubtituloHero() { return subtituloHero; }
    public void setSubtituloHero(String v) { this.subtituloHero = v; }

    public String getTextoBanner() { return textoBanner; }
    public void setTextoBanner(String v) { this.textoBanner = v; }

    public String getColorMarca() { return colorMarca; }
    public void setColorMarca(String v) { this.colorMarca = v; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String v) { this.logoUrl = v; }
}