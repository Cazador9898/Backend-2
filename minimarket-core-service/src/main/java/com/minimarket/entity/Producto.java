package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Schema(description = "Entidad que representa un producto disponible en el minimarket.")
@Entity
public class Producto {
    @Schema(description = "Identificador único del producto", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del producto", example = "Arroz 1kg")
    @Column(nullable = false)
    @NotBlank @Size(max = 120)
    private String nombre;

    @Schema(description = "Precio unitario del producto", example = "1490")
    @Column(nullable = false)
    @NotNull @Positive
    private Double precio;

    @Schema(description = "Cantidad disponible en stock", example = "50")
    @Column(nullable = false)
    @NotNull @PositiveOrZero
    private Integer stock;

    @Schema(description = "Categoría a la que pertenece el producto")
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
