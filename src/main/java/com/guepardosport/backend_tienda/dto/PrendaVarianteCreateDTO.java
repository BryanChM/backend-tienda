package com.guepardosport.backend_tienda.dto;

public class PrendaVarianteCreateDTO {
    private String talla;
    private Integer stock;
    private String sku;

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
}