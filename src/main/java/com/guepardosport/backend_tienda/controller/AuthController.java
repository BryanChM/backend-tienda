package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.dto.*;
import com.guepardosport.backend_tienda.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public AuthResponseDTO registrar(@Valid  @RequestBody RegistroClienteDTO dto) {
        return authService.registrarCliente(dto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        return authService.loginCliente(dto);
    }

    @PostMapping("/admin/login")
    public AuthResponseDTO loginAdmin(@Valid @RequestBody LoginRequestDTO dto) {
        return authService.loginAdmin(dto);
    }
    @GetMapping("/me")
    public Map<String, String> me(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            throw new RuntimeException("No autenticado");
        }
        String rol = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("DESCONOCIDO");

        return Map.of(
                "correo", authentication.getName(),
                "rol", rol
        );
    }
    @PostMapping("/recuperar")
    public Map<String, String> recuperar(@Valid @RequestBody SolicitarRecuperacionDTO dto) {
        authService.solicitarRecuperacion(dto);
        return Map.of("mensaje", "Si el correo existe, te enviamos un enlace de recuperación.");
    }

    @PostMapping("/restablecer")
    public Map<String, String> restablecer(@Valid @RequestBody RestablecerPasswordDTO dto) {
        authService.restablecerPassword(dto);
        return Map.of("mensaje", "Contraseña actualizada correctamente.");
    }

}