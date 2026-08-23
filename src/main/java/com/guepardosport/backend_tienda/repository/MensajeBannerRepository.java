package com.guepardosport.backend_tienda.repository;
import com.guepardosport.backend_tienda.entity.MensajeBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MensajeBannerRepository extends JpaRepository<MensajeBanner, Long> {
    List<MensajeBanner> findAllByOrderByOrdenAsc();
}