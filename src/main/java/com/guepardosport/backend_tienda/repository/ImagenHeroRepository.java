
package com.guepardosport.backend_tienda.repository;
import com.guepardosport.backend_tienda.entity.ImagenHero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ImagenHeroRepository extends JpaRepository<ImagenHero, Long> {
    List<ImagenHero> findAllByOrderByOrdenAsc();
}