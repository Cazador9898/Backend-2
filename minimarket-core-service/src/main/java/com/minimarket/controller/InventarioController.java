package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.hateoas.InventarioModelAssembler;
import com.minimarket.service.InventarioService;
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

@Tag(name = "Inventario", description = "Registro y consulta de movimientos de stock.")
@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
    private final InventarioService inventarioService;
    private final InventarioModelAssembler assembler;

    public InventarioController(InventarioService inventarioService, InventarioModelAssembler assembler) {
        this.inventarioService = inventarioService;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar movimientos de inventario")
    @GetMapping
    public CollectionModel<EntityModel<Inventario>> listarInventario() {
        List<EntityModel<Inventario>> modelos = inventarioService.findAll().stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos, linkTo(methodOn(InventarioController.class).listarInventario()).withSelfRel());
    }

    @Operation(summary = "Buscar movimiento por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Movimiento encontrado"), @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> obtenerInventarioPorId(@PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        return inventario == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(assembler.toModel(inventario));
    }

    @Operation(summary = "Listar movimientos de un producto")
    @GetMapping("/producto/{productoId}")
    public CollectionModel<EntityModel<Inventario>> listarPorProducto(@PathVariable Long productoId) {
        List<EntityModel<Inventario>> modelos = inventarioService.findByProductoId(productoId).stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos,
                linkTo(methodOn(InventarioController.class).listarPorProducto(productoId)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).obtenerProductoPorId(productoId)).withRel("producto"));
    }

    @Operation(summary = "Registrar movimiento de inventario")
    @PostMapping
    public ResponseEntity<EntityModel<Inventario>> guardarInventario(@Valid @RequestBody Inventario inventario) {
        Inventario guardado = inventarioService.save(inventario);
        URI location = linkTo(methodOn(InventarioController.class).obtenerInventarioPorId(guardado.getId())).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(guardado));
    }

    @Operation(summary = "Actualizar movimiento de inventario")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> actualizarInventario(@PathVariable Long id, @Valid @RequestBody Inventario inventario) {
        if (inventarioService.findById(id) == null) return ResponseEntity.notFound().build();
        inventario.setId(id);
        return ResponseEntity.ok(assembler.toModel(inventarioService.save(inventario)));
    }

    @Operation(summary = "Eliminar movimiento de inventario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long id) {
        if (inventarioService.findById(id) == null) return ResponseEntity.notFound().build();
        inventarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
