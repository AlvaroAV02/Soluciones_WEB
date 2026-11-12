package com.upn.ecommerce.controller;

import com.upn.ecommerce.dto.ProductoDTO;
import com.upn.ecommerce.service.ProductoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
@SecurityRequirement(name = "bearerAuth")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoDTO> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public ProductoDTO obtener(@PathVariable Long id) { return service.obtener(id); }

    @GetMapping("/categoria/{categoria}")
    public List<ProductoDTO> porCategoria(@PathVariable String categoria) { return service.porCategoria(categoria); }

    @GetMapping("/buscar")
    public List<ProductoDTO> buscar(@RequestParam String nombre) { return service.buscarPorNombre(nombre); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDTO crear(@Valid @RequestBody ProductoDTO dto) { return service.crear(dto); }

    @PutMapping("/{id}")
    public ProductoDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProductoDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
