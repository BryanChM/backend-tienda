package com.guepardosport.backend_tienda.security;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginAttemptService {

    private static final int MAX_INTENTOS = 5;
    private static final int MINUTOS_BLOQUEO = 15;

    private static class Registro {
        int intentos = 0;
        LocalDateTime bloqueadoHasta = null;
    }

    private final ConcurrentHashMap<String, Registro> intentos = new ConcurrentHashMap<>();

    public void verificarNoBloqueado(String correo) {
        Registro r = intentos.get(correo.toLowerCase());
        if (r != null && r.bloqueadoHasta != null) {
            if (LocalDateTime.now().isBefore(r.bloqueadoHasta)) {
                throw new RuntimeException("Cuenta bloqueada temporalmente por intentos fallidos. Intenta de nuevo más tarde.");
            } else {
                intentos.remove(correo.toLowerCase());
            }
        }
    }

    public void registrarFallo(String correo) {
        Registro r = intentos.computeIfAbsent(correo.toLowerCase(), k -> new Registro());
        r.intentos++;
        if (r.intentos >= MAX_INTENTOS) {
            r.bloqueadoHasta = LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO);
        }
    }

    public void registrarExito(String correo) {
        intentos.remove(correo.toLowerCase());
    }
}