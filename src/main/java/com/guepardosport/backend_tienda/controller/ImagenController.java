package com.guepardosport.backend_tienda.controller;

import com.guepardosport.backend_tienda.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/imagenes")
@CrossOrigin(origins = "http://localhost:4200")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @PostMapping("/subir")
    public Map<String, String> subir(@RequestParam("archivo") MultipartFile archivo) {
        String url = imagenService.subirImagen(archivo);
        return Map.of("url", url);
    }
}