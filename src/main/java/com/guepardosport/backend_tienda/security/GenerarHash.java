package com.guepardosport.backend_tienda.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordPlano = "admin123"; // cambia esto por la contraseña que quieras
        String hash = encoder.encode(passwordPlano);
        System.out.println("Hash generado: " + hash);
    }
}

