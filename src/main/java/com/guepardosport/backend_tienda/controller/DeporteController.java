package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.entity.Deporte;
import com.guepardosport.backend_tienda.service.DeporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/deportes")
@CrossOrigin(origins = "http://localhost:4200")
public class DeporteController {

    @Autowired
    private DeporteService deporteService;

    @GetMapping
    public List<Deporte> listar() {
        return deporteService.listarTodos();
    }

    @PostMapping
    public Deporte crear(@RequestBody Deporte deporte) {
        return deporteService.crear(deporte);
    }

    @PutMapping("/{id}")
    public Deporte actualizar(@PathVariable Long id, @RequestBody Deporte deporte) {
        return deporteService.actualizar(id, deporte);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        deporteService.eliminar(id);
    }
}