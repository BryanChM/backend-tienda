package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.entity.ConfiguracionSitio;
import com.guepardosport.backend_tienda.repository.ConfiguracionSitioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfiguracionController {

    @Autowired
    private ConfiguracionSitioRepository repo;

    @GetMapping
    public ConfiguracionSitio obtener() {
        return repo.findById(1L).orElseGet(() -> {
            ConfiguracionSitio c = new ConfiguracionSitio();
            c.setId(1L);
            return repo.save(c);
        });
    }

    @PutMapping
    public ConfiguracionSitio actualizar(@RequestBody ConfiguracionSitio datos) {
        ConfiguracionSitio actual = obtener();
        actual.setTituloHero(datos.getTituloHero());
        actual.setSubtituloHero(datos.getSubtituloHero());
        actual.setTextoBanner(datos.getTextoBanner());
        actual.setColorMarca(datos.getColorMarca());
        actual.setLogoUrl(datos.getLogoUrl());
        return repo.save(actual);
    }
}