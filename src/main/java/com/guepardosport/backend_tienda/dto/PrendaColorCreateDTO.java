package com.guepardosport.backend_tienda.dto;

import java.util.List;

public class PrendaColorCreateDTO {
    private String color;
    private List<String> imagenes;       // URLs de las fotos de este color
    private List<PrendaVarianteCreateDTO> variantes; // tallas y su stock

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public List<String> getImagenes() { return imagenes; }
    public void setImagenes(List<String> imagenes) { this.imagenes = imagenes; }

    public List<PrendaVarianteCreateDTO> getVariantes() { return variantes; }
    public void setVariantes(List<PrendaVarianteCreateDTO> variantes) { this.variantes = variantes; }
}