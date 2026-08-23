package com.guepardosport.backend_tienda.repository;

import com.guepardosport.backend_tienda.entity.Prenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrendaRepository extends JpaRepository<Prenda, Long> {
}