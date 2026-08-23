package com.guepardosport.backend_tienda.dto;

import java.util.List;

public class PrendaColorDTO {
    private Long id;
    private String color;
    private List<String> imagenes;
    private List<PrendaVarianteDTO> variantes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public List<String> getImagenes() { return imagenes; }
    public void setImagenes(List<String> imagenes) { this.imagenes = imagenes; }

    public List<PrendaVarianteDTO> getVariantes() { return variantes; }
    public void setVariantes(List<PrendaVarianteDTO> variantes) { this.variantes = variantes; }
}