package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.entity.ImagenHero;
import com.guepardosport.backend_tienda.repository.ImagenHeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/imagenes-hero")
@CrossOrigin(origins = "http://localhost:4200")
public class ImagenHeroController {

    @Autowired private ImagenHeroRepository repo;

    @GetMapping
    public List<ImagenHero> listar() {
        return repo.findAllByOrderByOrdenAsc();
    }

    @PostMapping
    public ImagenHero crear(@RequestBody ImagenHero imagen) {
        return repo.save(imagen);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}