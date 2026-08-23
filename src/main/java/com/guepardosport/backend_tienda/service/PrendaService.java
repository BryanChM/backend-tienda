package com.guepardosport.backend_tienda.service;

import com.guepardosport.backend_tienda.dto.*;
import com.guepardosport.backend_tienda.entity.*;
import com.guepardosport.backend_tienda.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PrendaService {

    @Autowired
    private PrendaRepository prendaRepository;

    @Autowired
    private DeporteRepository deporteRepository;

    public List<PrendaResponseDTO> listarTodas() {
        return prendaRepository.findAll()
                .stream()
                .map(this::convertirADTO )
                .collect(Collectors.toList());
    }

    public PrendaResponseDTO obtenerPorId(Long id) {
        Prenda prenda = prendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada con id " + id));
        return convertirADTO(prenda);
    }

    public PrendaResponseDTO crear(PrendaCreateDTO dto) {
        Prenda prenda = new Prenda();
        prenda.setNombre(dto.getNombre());
        prenda.setDescripcion(dto.getDescripcion());
        prenda.setPrecioBase(dto.getPrecioBase());
        prenda.setGenero(normalizarGenero(dto.getGenero()));   // ← agregar esta línea aquí

        if (dto.getDeporteIds() != null) {
            Set<Deporte> deportes = new HashSet<>(deporteRepository.findAllById(dto.getDeporteIds()));
            prenda.setDeportes(deportes);
        }

        Prenda guardada = prendaRepository.save(prenda);
        return convertirADTO(guardada);
    }

    // --- Conversión Entity -> DTO ---
    private PrendaResponseDTO convertirADTO(Prenda prenda) {
        PrendaResponseDTO dto = new PrendaResponseDTO();
        dto.setId(prenda.getId());
        dto.setNombre(prenda.getNombre());
        dto.setDescripcion(prenda.getDescripcion());
        dto.setPrecioBase(prenda.getPrecioBase());
        dto.setPrecioConIva(
                prenda.getPrecioBase().multiply(new BigDecimal("1.12")).setScale(2, RoundingMode.HALF_UP)
        );
        dto.setEstado(prenda.getEstado());
        dto.setGenero(prenda.getGenero());

        dto.setDeportes(
                prenda.getDeportes().stream().map(Deporte::getNombre).collect(Collectors.toList())
        );

        if (prenda.getColores() != null) {
            dto.setColores(
                    prenda.getColores().stream().map(this::convertirColorADTO).collect(Collectors.toList())
            );
        }

        return dto;
    }

    private PrendaColorDTO convertirColorADTO(PrendaColor color) {
        PrendaColorDTO dto = new PrendaColorDTO();
        dto.setId(color.getId());
        dto.setColor(color.getColor());

        if (color.getImagenes() != null) {
            dto.setImagenes(
                    color.getImagenes().stream().map(PrendaImagen::getUrlImagen).collect(Collectors.toList())
            );
        }

        if (color.getVariantes() != null) {
            dto.setVariantes(
                    color.getVariantes().stream().map(v -> {
                        PrendaVarianteDTO vDto = new PrendaVarianteDTO();
                        vDto.setId(v.getId());
                        vDto.setTalla(v.getTalla());
                        vDto.setStock(v.getStock());
                        vDto.setSku(v.getSku());
                        return vDto;
                    }).collect(Collectors.toList())
            );
        }

        return dto;
    }
    @Autowired
    private PrendaColorRepository prendaColorRepository;

    @Autowired
    private PrendaImagenRepository prendaImagenRepository;

    @Autowired
    private PrendaVarianteRepository prendaVarianteRepository;

    public PrendaColorDTO agregarColor(Long prendaId, PrendaColorCreateDTO dto) {
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada con id " + prendaId));

        PrendaColor color = new PrendaColor();
        color.setPrenda(prenda);
        color.setColor(dto.getColor());
        PrendaColor colorGuardado = prendaColorRepository.save(color);

        if (dto.getImagenes() != null) {
            int orden = 0;
            for (String url : dto.getImagenes()) {
                PrendaImagen imagen = new PrendaImagen();
                imagen.setPrendaColor(colorGuardado);
                imagen.setUrlImagen(url);
                imagen.setOrden(orden);
                imagen.setEsPrincipal(orden == 0); // la primera foto es la principal
                prendaImagenRepository.save(imagen);
                orden++;
            }
        }

        if (dto.getVariantes() != null) {
            for (PrendaVarianteCreateDTO vDto : dto.getVariantes()) {
                PrendaVariante variante = new PrendaVariante();
                variante.setPrendaColor(colorGuardado);
                variante.setTalla(vDto.getTalla());
                variante.setStock(vDto.getStock());
                variante.setSku(vDto.getSku());
                prendaVarianteRepository.save(variante);
            }
        }

        // Recargamos el color desde la BD para traer imágenes y variantes ya asociadas
        PrendaColor colorCompleto = prendaColorRepository.findById(colorGuardado.getId()).orElseThrow();
        return convertirColorADTO(colorCompleto);
    }
    public PrendaResponseDTO actualizar(Long id, PrendaCreateDTO dto) {
        Prenda prenda = prendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada con id " + id));

        prenda.setNombre(dto.getNombre());
        prenda.setDescripcion(dto.getDescripcion());
        prenda.setPrecioBase(dto.getPrecioBase());
        prenda.setGenero(normalizarGenero(dto.getGenero()));   // ← agregar esta línea aquí

        if (dto.getDeporteIds() != null) {
            java.util.Set<Deporte> deportes = new HashSet<>(deporteRepository.findAllById(dto.getDeporteIds()));
            prenda.setDeportes(deportes);
        }

        return convertirADTO(prendaRepository.save(prenda));
    }

    public void eliminar(Long id) {
        if (!prendaRepository.existsById(id)) {
            throw new RuntimeException("Prenda no encontrada con id " + id);
        }
        prendaRepository.deleteById(id);
    }
    private static final List<String> GENEROS_VALIDOS = List.of("MASCULINO", "FEMENINO", "UNISEX");

    private String normalizarGenero(String genero) {
        if (genero == null || !GENEROS_VALIDOS.contains(genero)) return "UNISEX";
        return genero;
    }
}