package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.hateoas.CategoriaModelAssembler;
import com.minimarket.service.CategoriaService;
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

@Tag(name = "Categorías", description = "Administración de categorías de productos.")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;
    private final CategoriaModelAssembler assembler;

    public CategoriaController(CategoriaService categoriaService, CategoriaModelAssembler assembler) {
        this.categoriaService = categoriaService;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar categorías")
    @GetMapping
    public CollectionModel<EntityModel<Categoria>> listarCategorias() {
        List<EntityModel<Categoria>> modelos = categoriaService.findAll().stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos, linkTo(methodOn(CategoriaController.class).listarCategorias()).withSelfRel());
    }

    @Operation(summary = "Buscar categoría por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Categoría encontrada"), @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Categoria>> obtenerCategoriaPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        return categoria == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(assembler.toModel(categoria));
    }

    @Operation(summary = "Crear categoría")
    @PostMapping
    public ResponseEntity<EntityModel<Categoria>> guardarCategoria(@Valid @RequestBody Categoria categoria) {
        Categoria guardada = categoriaService.save(categoria);
        URI location = linkTo(methodOn(CategoriaController.class).obtenerCategoriaPorId(guardada.getId())).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(guardada));
    }

    @Operation(summary = "Actualizar categoría")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Categoria>> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        if (categoriaService.findById(id) == null) return ResponseEntity.notFound().build();
        categoria.setId(id);
        return ResponseEntity.ok(assembler.toModel(categoriaService.save(categoria)));
    }

    @Operation(summary = "Eliminar categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        if (categoriaService.findById(id) == null) return ResponseEntity.notFound().build();
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
