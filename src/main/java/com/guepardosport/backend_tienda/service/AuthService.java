package com.guepardosport.backend_tienda.service;

import com.guepardosport.backend_tienda.dto.*;
import com.guepardosport.backend_tienda.entity.Administrador;
import com.guepardosport.backend_tienda.entity.Cliente;
import com.guepardosport.backend_tienda.repository.AdministradorRepository;
import com.guepardosport.backend_tienda.repository.ClienteRepository;
import com.guepardosport.backend_tienda.security.JwtUtil;
import com.guepardosport.backend_tienda.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private ClienteRepository clienteRepository;
    @Autowired private AdministradorRepository administradorRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private EmailService emailService;
    @Autowired private LoginAttemptService loginAttemptService;


    public AuthResponseDTO registrarCliente(RegistroClienteDTO dto) {
        validarPassword(dto.getPassword());
        if (clienteRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new RuntimeException("Ya existe una cuenta con ese correo");

        }
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setCorreo(dto.getCorreo());
        cliente.setHashPassword(passwordEncoder.encode(dto.getPassword()));
        clienteRepository.save(cliente);
        emailService.enviarBienvenida(cliente);


        String token = jwtUtil.generarToken(cliente.getCorreo(), "CLIENTE");
        return new AuthResponseDTO(token, cliente.getNombre(), cliente.getCorreo(), "CLIENTE");


    }





    public AuthResponseDTO loginCliente(LoginRequestDTO dto) {
        loginAttemptService.verificarNoBloqueado(dto.getCorreo());

        Cliente cliente = clienteRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> { loginAttemptService.registrarFallo(dto.getCorreo()); return new RuntimeException("Credenciales inválidas"); });

        if (!passwordEncoder.matches(dto.getPassword(), cliente.getHashPassword())) {
            loginAttemptService.registrarFallo(dto.getCorreo());
            throw new RuntimeException("Credenciales inválidas");
        }

        loginAttemptService.registrarExito(dto.getCorreo());
        String token = jwtUtil.generarToken(cliente.getCorreo(), "CLIENTE");
        return new AuthResponseDTO(token, cliente.getNombre(), cliente.getCorreo(), "CLIENTE");
    }

    public AuthResponseDTO loginAdmin(LoginRequestDTO dto) {
        loginAttemptService.verificarNoBloqueado(dto.getCorreo());

        Administrador admin = administradorRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> { loginAttemptService.registrarFallo(dto.getCorreo()); return new RuntimeException("Credenciales inválidas"); });

        if (!passwordEncoder.matches(dto.getPassword(), admin.getHashPassword())) {
            loginAttemptService.registrarFallo(dto.getCorreo());
            throw new RuntimeException("Credenciales inválidas");
        }

        loginAttemptService.registrarExito(dto.getCorreo());
        String token = jwtUtil.generarToken(admin.getCorreo(), admin.getRol());
        return new AuthResponseDTO(token, admin.getNombre(), admin.getCorreo(), admin.getRol());
    }
    private void validarPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            throw new RuntimeException("La contraseña debe incluir al menos una letra y un número");
        }
    }
    public void solicitarRecuperacion(SolicitarRecuperacionDTO dto) {
        Cliente cliente = clienteRepository.findByCorreo(dto.getCorreo()).orElse(null);

        // No revelamos si el correo existe o no, por seguridad — siempre respondemos igual al frontend
        if (cliente == null) return;

        String token = java.util.UUID.randomUUID().toString();
        cliente.setResetToken(token);
        cliente.setResetTokenExpira(java.time.LocalDateTime.now().plusMinutes(30));
        clienteRepository.save(cliente);

        emailService.enviarRecuperacionPassword(cliente, token);
    }

    public void restablecerPassword(RestablecerPasswordDTO dto) {
        Cliente cliente = clienteRepository.findByResetToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Enlace inválido o expirado"));

        if (cliente.getResetTokenExpira() == null || java.time.LocalDateTime.now().isAfter(cliente.getResetTokenExpira())) {
            throw new RuntimeException("Enlace inválido o expirado");
        }

        validarPassword(dto.getNuevaPassword());

        cliente.setHashPassword(passwordEncoder.encode(dto.getNuevaPassword()));
        cliente.setResetToken(null);
        cliente.setResetTokenExpira(null);
        clienteRepository.save(cliente);
    }

}