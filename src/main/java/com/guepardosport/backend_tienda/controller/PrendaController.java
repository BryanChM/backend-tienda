package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.dto.PrendaColorCreateDTO;
import com.guepardosport.backend_tienda.dto.PrendaColorDTO;
import com.guepardosport.backend_tienda.dto.PrendaCreateDTO;
import com.guepardosport.backend_tienda.dto.PrendaResponseDTO;
import com.guepardosport.backend_tienda.service.PrendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prendas")
@CrossOrigin(origins = "http://localhost:4200")
public class PrendaController {

    @Autowired
    private PrendaService prendaService;

    @GetMapping
    public List<PrendaResponseDTO> listar() {
        return prendaService.listarTodas();
    }

    @GetMapping("/{id}")
    public PrendaResponseDTO obtener(@PathVariable Long id) {
        return prendaService.obtenerPorId(id);
    }

    @PostMapping
    public PrendaResponseDTO crear(@Valid @RequestBody PrendaCreateDTO dto) {
        return prendaService.crear(dto);
    }
    @PostMapping("/{id}/colores")
    public PrendaColorDTO agregarColor(@PathVariable Long id, @RequestBody PrendaColorCreateDTO dto) {
        return prendaService.agregarColor(id, dto);
    }
    @PutMapping("/{id}")
    public PrendaResponseDTO actualizar(@PathVariable Long id, @RequestBody PrendaCreateDTO dto) {
        return prendaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        prendaService.eliminar(id);
    }
}