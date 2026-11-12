package com.upn.ecommerce.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductoDTO(
    Long id,
    @NotBlank String nombre,
    @NotBlank String categoria,
    @NotNull @DecimalMin("0.0") BigDecimal precioCompra,
    @NotNull @DecimalMin("0.0") BigDecimal precioVenta,
    @NotNull @Min(0) Integer stock,
    String marca,
    @NotBlank String codigoBarras,
    LocalDate fechaIngreso
) {}
