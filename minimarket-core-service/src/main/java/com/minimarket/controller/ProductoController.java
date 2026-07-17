package com.minimarket.controller;

import com.minimarket.entity.Producto;
import com.minimarket.hateoas.ProductoModelAssembler;
import com.minimarket.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Productos", description = "Gestión completa del catálogo de productos.")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService productoService;
    private final ProductoModelAssembler assembler;

    public ProductoController(ProductoService productoService, ProductoModelAssembler assembler) {
        this.productoService = productoService;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar productos", description = "Obtiene todos los productos con enlaces HATEOAS para consultar cada recurso.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public CollectionModel<EntityModel<Producto>> listarProductos() {
        List<EntityModel<Producto>> modelos = productoService.findAll().stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos, linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());
    }

    @Operation(summary = "Buscar producto por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Producto encontrado"), @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerProductoPorId(@Parameter(example = "1") @PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return producto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(assembler.toModel(producto));
    }

    @Operation(summary = "Listar productos por categoría")
    @GetMapping("/categoria/{categoriaId}")
    public CollectionModel<EntityModel<Producto>> listarPorCategoria(@PathVariable Long categoriaId) {
        List<EntityModel<Producto>> modelos = productoService.findByCategoriaId(categoriaId).stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos,
                linkTo(methodOn(ProductoController.class).listarPorCategoria(categoriaId)).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).obtenerCategoriaPorId(categoriaId)).withRel("categoria"));
    }

    @Operation(summary = "Crear producto")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Producto creado"), @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)})
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> guardarProducto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Producto.class), examples = @ExampleObject(value = "{\"nombre\":\"Arroz 1kg\",\"precio\":1490,\"stock\":50,\"categoria\":{\"id\":1}}")))
            @Valid @RequestBody Producto producto) {
        Producto guardado = productoService.save(producto);
        URI location = linkTo(methodOn(ProductoController.class).obtenerProductoPorId(guardado.getId())).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(guardado));
    }

    @Operation(summary = "Actualizar producto")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        if (productoService.findById(id) == null) return ResponseEntity.notFound().build();
        producto.setId(id);
        return ResponseEntity.ok(assembler.toModel(productoService.save(producto)));
    }

    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoService.findById(id) == null) return ResponseEntity.notFound().build();
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
