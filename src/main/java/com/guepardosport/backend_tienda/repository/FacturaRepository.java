package com.guepardosport.backend_tienda.repository;

import com.guepardosport.backend_tienda.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByPedidoId(Long pedidoId);
    long countByNumeroFacturaStartingWith(String prefijo);
}