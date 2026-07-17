package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.hateoas.DetalleVentaModelAssembler;
import com.minimarket.service.DetalleVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Detalles de venta", description = "Productos y cantidades asociados a cada venta.")
@RestController
@RequestMapping("/api/detalles-venta")
public class DetalleVentaController {
    private final DetalleVentaService detalleVentaService;
    private final DetalleVentaModelAssembler assembler;

    public DetalleVentaController(DetalleVentaService detalleVentaService, DetalleVentaModelAssembler assembler) {
        this.detalleVentaService = detalleVentaService;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar detalles de venta")
    @GetMapping
    public CollectionModel<EntityModel<DetalleVenta>> listarDetalles() {
        List<EntityModel<DetalleVenta>> modelos = detalleVentaService.findAll().stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos, linkTo(methodOn(DetalleVentaController.class).listarDetalles()).withSelfRel());
    }

    @Operation(summary = "Buscar detalle por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Detalle encontrado"), @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DetalleVenta>> obtenerDetallePorId(@PathVariable Long id) {
        DetalleVenta detalle = detalleVentaService.findById(id);
        return detalle == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(assembler.toModel(detalle));
    }

    @Operation(summary = "Listar detalles de una venta")
    @GetMapping("/venta/{ventaId}")
    public CollectionModel<EntityModel<DetalleVenta>> listarPorVenta(@PathVariable Long ventaId) {
        List<EntityModel<DetalleVenta>> modelos = detalleVentaService.findByVentaId(ventaId).stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos,
                linkTo(methodOn(DetalleVentaController.class).listarPorVenta(ventaId)).withSelfRel(),
                linkTo(methodOn(VentaController.class).obtenerVentaPorId(ventaId)).withRel("venta"));
    }

    @Operation(summary = "Crear detalle de venta")
    @PostMapping
    public ResponseEntity<EntityModel<DetalleVenta>> guardarDetalle(@Valid @RequestBody DetalleVenta detalle) {
        DetalleVenta guardado = detalleVentaService.save(detalle);
        URI location = linkTo(methodOn(DetalleVentaController.class).obtenerDetallePorId(guardado.getId())).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(guardado));
    }

    @Operation(summary = "Actualizar detalle de venta")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<DetalleVenta>> actualizarDetalle(@PathVariable Long id, @Valid @RequestBody DetalleVenta detalle) {
        if (detalleVentaService.findById(id) == null) return ResponseEntity.notFound().build();
        detalle.setId(id);
        return ResponseEntity.ok(assembler.toModel(detalleVentaService.save(detalle)));
    }

    @Operation(summary = "Eliminar detalle de venta")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long id) {
        if (detalleVentaService.findById(id) == null) return ResponseEntity.notFound().build();
        detalleVentaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
