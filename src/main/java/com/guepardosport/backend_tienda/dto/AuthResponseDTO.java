package com.guepardosport.backend_tienda.dto;

public class AuthResponseDTO {
    private String token;
    private String nombre;
    private String correo;
    private String rol;

    public AuthResponseDTO(String token, String nombre, String correo, String rol) {
        this.token = token;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public String getToken() { return token; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getRol() { return rol; }
}