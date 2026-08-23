package com.guepardosport.backend_tienda.repository;

import com.guepardosport.backend_tienda.entity.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeporteRepository extends JpaRepository<Deporte, Long> {
}