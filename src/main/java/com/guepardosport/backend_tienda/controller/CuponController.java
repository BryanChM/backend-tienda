package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.dto.CuponDTO;
import com.guepardosport.backend_tienda.service.CuponService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cupones")
@CrossOrigin(origins = "http://localhost:4200")
public class CuponController {

    @Autowired
    private CuponService cuponService;

    @GetMapping
    public List<CuponDTO> listar() {
        return cuponService.listarTodos();
    }

    @GetMapping("/{id}")
    public CuponDTO obtener(@PathVariable Long id) {
        return cuponService.obtenerPorId(id);
    }

    @PostMapping
    public CuponDTO crear(@Valid @RequestBody CuponDTO dto) {
        return cuponService.crear(dto);
    }

    @PutMapping("/{id}")
    public CuponDTO actualizar(@PathVariable Long id, @RequestBody CuponDTO dto) {
        return cuponService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        cuponService.eliminar(id);
    }
}