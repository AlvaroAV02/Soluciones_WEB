package com.upn.ecommerce.repository;

import com.upn.ecommerce.entity.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Usan las Named Queries "Producto.findByCategoria" y "Producto.findByCodigoBarras" de la entidad
    List<Producto> findByCategoria(@Param("categoria") String categoria);

    Optional<Producto> findByCodigoBarras(@Param("codigoBarras") String codigoBarras);

    // JPQL con parámetros nombrados (inyección segura: nunca se concatena texto del usuario)
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Producto> buscarPorNombre(@Param("nombre") String nombre);

    boolean existsByCodigoBarras(String codigoBarras);
}
