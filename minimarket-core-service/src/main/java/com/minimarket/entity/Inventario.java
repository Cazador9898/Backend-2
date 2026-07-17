package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Date;

@Schema(description = "Entidad que representa un movimiento de inventario de entrada o salida.")
@Entity
public class Inventario {
    @Schema(description = "Identificador único del movimiento", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Producto asociado al movimiento de inventario")
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Schema(description = "Cantidad involucrada en el movimiento", example = "10")
    @Column(nullable = false)
    @NotNull @Positive
    private Integer cantidad;

    @Schema(description = "Tipo de movimiento realizado", example = "Entrada")
    @Column(nullable = false)
    @NotBlank
    private String tipoMovimiento;

    @Schema(description = "Fecha en la que se registra el movimiento", example = "2026-06-26T00:00:00.000+00:00")
    @Column(nullable = false)
    @NotNull
    private Date fechaMovimiento;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public Date getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(Date fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
}
