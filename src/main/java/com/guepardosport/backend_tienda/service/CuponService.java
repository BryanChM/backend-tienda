package com.guepardosport.backend_tienda.service;

import com.guepardosport.backend_tienda.dto.CuponDTO;
import com.guepardosport.backend_tienda.entity.Cupon;
import com.guepardosport.backend_tienda.repository.CuponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuponService {

    @Autowired
    private CuponRepository cuponRepository;

    public List<CuponDTO> listarTodos() {
        return cuponRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public CuponDTO obtenerPorId(Long id) {
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado con id " + id));
        return convertirADTO(cupon);
    }

    public CuponDTO crear(CuponDTO dto) {
        if (cuponRepository.findByCodigo(dto.getCodigo()).isPresent()) {
            throw new RuntimeException("Ya existe un cupón con el código " + dto.getCodigo());
        }
        Cupon cupon = new Cupon();
        aplicarDatos(cupon, dto);
        return convertirADTO(cuponRepository.save(cupon));
    }

    public CuponDTO actualizar(Long id, CuponDTO dto) {
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado con id " + id));
        aplicarDatos(cupon, dto);
        return convertirADTO(cuponRepository.save(cupon));
    }

    public void eliminar(Long id) {
        if (!cuponRepository.existsById(id)) {
            throw new RuntimeException("Cupón no encontrado con id " + id);
        }
        cuponRepository.deleteById(id);
    }

    // --- Validación de cupón para el checkout (la usaremos más adelante en CU-04) ---
    public Cupon validarCuponParaUso(String codigo, java.math.BigDecimal totalCarrito) {
        Cupon cupon = cuponRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Cupón no válido"));

        if (!cupon.getActivo()) throw new RuntimeException("Este cupón ya no está activo");


        {
            java.time.LocalDate hoy = java.time.LocalDate.now();
            if (hoy.isBefore(cupon.getFechaInicio()) || hoy.isAfter(cupon.getFechaFin())) {
                throw new RuntimeException("Este cupón no está vigente");
            }
        }

        if (cupon.getLimiteUso() != null && cupon.getUsosActuales() >= cupon.getLimiteUso()) {
            throw new RuntimeException("Este cupón alcanzó su límite de usos");
        }

        if (cupon.getMontoMinimo() != null && totalCarrito.compareTo(cupon.getMontoMinimo()) < 0) {
            throw new RuntimeException("El monto mínimo para este cupón es " + cupon.getMontoMinimo());
        }

        return cupon;
    }

    private void aplicarDatos(Cupon cupon, CuponDTO dto) {
        cupon.setCodigo(dto.getCodigo());
        cupon.setTipoDescuento(dto.getTipoDescuento());
        cupon.setValor(dto.getValor());
        cupon.setMontoMinimo(dto.getMontoMinimo());
        cupon.setFechaInicio(dto.getFechaInicio());
        cupon.setFechaFin(dto.getFechaFin());
        cupon.setLimiteUso(dto.getLimiteUso());
        if (dto.getActivo() != null) cupon.setActivo(dto.getActivo());
    }

    private CuponDTO convertirADTO(Cupon cupon) {
        CuponDTO dto = new CuponDTO();
        dto.setId(cupon.getId());
        dto.setCodigo(cupon.getCodigo());
        dto.setTipoDescuento(cupon.getTipoDescuento());
        dto.setValor(cupon.getValor());
        dto.setMontoMinimo(cupon.getMontoMinimo());
        dto.setFechaInicio(cupon.getFechaInicio());
        dto.setFechaFin(cupon.getFechaFin());
        dto.setLimiteUso(cupon.getLimiteUso());
        dto.setUsosActuales(cupon.getUsosActuales());
        dto.setActivo(cupon.getActivo());
        return dto;
    }
}