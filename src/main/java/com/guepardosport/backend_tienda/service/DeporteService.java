package com.guepardosport.backend_tienda.service;

import com.guepardosport.backend_tienda.entity.Deporte;
import com.guepardosport.backend_tienda.repository.DeporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeporteService {

    @Autowired
    private DeporteRepository deporteRepository;

    public List<Deporte> listarTodos() {
        return deporteRepository.findAll();
    }

    public Deporte crear(Deporte deporte) {
        return deporteRepository.save(deporte);
    }
    public Deporte actualizar(Long id, Deporte datos) {
        Deporte deporte = deporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
        deporte.setNombre(datos.getNombre());
        return deporteRepository.save(deporte);
    }

    public void eliminar(Long id) {
        if (!deporteRepository.existsById(id)) {
            throw new RuntimeException("Deporte no encontrado");
        }
        deporteRepository.deleteById(id);
    }
}