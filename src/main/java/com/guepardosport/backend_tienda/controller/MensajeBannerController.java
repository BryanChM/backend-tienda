package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.entity.MensajeBanner;
import com.guepardosport.backend_tienda.repository.MensajeBannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/banner-mensajes")
@CrossOrigin(origins = "http://localhost:4200")
public class MensajeBannerController {

    @Autowired private MensajeBannerRepository repo;

    @GetMapping
    public List<MensajeBanner> listar() {
        return repo.findAllByOrderByOrdenAsc();
    }

    @PostMapping
    public MensajeBanner crear(@RequestBody MensajeBanner mensaje) {
        return repo.save(mensaje);
    }

    @PutMapping("/{id}")
    public MensajeBanner actualizar(@PathVariable Long id, @RequestBody MensajeBanner datos) {
        MensajeBanner m = repo.findById(id).orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        m.setTexto(datos.getTexto());
        m.setOrden(datos.getOrden());
        return repo.save(m);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}