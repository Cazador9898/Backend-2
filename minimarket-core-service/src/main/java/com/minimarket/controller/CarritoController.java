package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.hateoas.CarritoModelAssembler;
import com.minimarket.service.CarritoService;
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

@Tag(name = "Carrito", description = "Operaciones para agregar, consultar, actualizar y eliminar productos del carrito.")
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    private final CarritoService carritoService;
    private final CarritoModelAssembler assembler;

    public CarritoController(CarritoService carritoService, CarritoModelAssembler assembler) {
        this.carritoService = carritoService;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar carrito")
    @GetMapping
    public CollectionModel<EntityModel<Carrito>> listarCarrito() {
        List<EntityModel<Carrito>> modelos = carritoService.findAll().stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos, linkTo(methodOn(CarritoController.class).listarCarrito()).withSelfRel());
    }

    @Operation(summary = "Buscar registro del carrito por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Registro encontrado"), @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Carrito>> obtenerCarritoPorId(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        return carrito == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(assembler.toModel(carrito));
    }

    @Operation(summary = "Listar carrito de un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public CollectionModel<EntityModel<Carrito>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<EntityModel<Carrito>> modelos = carritoService.findByUsuarioId(usuarioId).stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos,
                linkTo(methodOn(CarritoController.class).listarPorUsuario(usuarioId)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(usuarioId)).withRel("usuario"));
    }

    @Operation(summary = "Agregar producto al carrito")
    @PostMapping
    public ResponseEntity<EntityModel<Carrito>> agregarProductoAlCarrito(@Valid @RequestBody Carrito carrito) {
        Carrito guardado = carritoService.save(carrito);
        URI location = linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(guardado.getId())).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(guardado));
    }

    @Operation(summary = "Actualizar carrito")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Carrito>> actualizarCarrito(@PathVariable Long id, @Valid @RequestBody Carrito carrito) {
        if (carritoService.findById(id) == null) return ResponseEntity.notFound().build();
        carrito.setId(id);
        return ResponseEntity.ok(assembler.toModel(carritoService.save(carrito)));
    }

    @Operation(summary = "Eliminar producto del carrito")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable Long id) {
        if (carritoService.findById(id) == null) return ResponseEntity.notFound().build();
        carritoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
