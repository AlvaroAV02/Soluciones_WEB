package com.upn.ecommerce.service;

import com.upn.ecommerce.dto.ProductoDTO;
import com.upn.ecommerce.entity.Producto;
import com.upn.ecommerce.repository.ProductoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtener(Long id) {
        return toDTO(buscar(id));
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> porCategoria(String categoria) {
        return repo.findByCategoria(categoria).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        return repo.buscarPorNombre(nombre).stream().map(this::toDTO).toList();
    }

    @Transactional
    public ProductoDTO crear(ProductoDTO dto) {
        if (repo.existsByCodigoBarras(dto.codigoBarras())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese código de barras");
        }
        Producto p = new Producto();
        copiar(dto, p);
        return toDTO(repo.save(p));
    }

    @Transactional
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto p = buscar(id);
        repo.findByCodigoBarras(dto.codigoBarras()).ifPresent(otro -> {
            if (!otro.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Código de barras en uso por otro producto");
            }
        });
        copiar(dto, p);
        return toDTO(repo.save(p));
    }

    @Transactional
    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    private Producto buscar(Long id) {
        return repo.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + id));
    }

    private void copiar(ProductoDTO d, Producto p) {
        p.setNombre(d.nombre());
        p.setCategoria(d.categoria());
        p.setPrecioCompra(d.precioCompra());
        p.setPrecioVenta(d.precioVenta());
        p.setStock(d.stock());
        p.setMarca(d.marca());
        p.setCodigoBarras(d.codigoBarras());
        p.setFechaIngreso(d.fechaIngreso() != null ? d.fechaIngreso() : LocalDate.now());
    }

    private ProductoDTO toDTO(Producto p) {
        return new ProductoDTO(p.getId(), p.getNombre(), p.getCategoria(), p.getPrecioCompra(),
            p.getPrecioVenta(), p.getStock(), p.getMarca(), p.getCodigoBarras(), p.getFechaIngreso());
    }
}
