package com.minimarket.controller;

import com.minimarket.entity.Venta;
import com.minimarket.hateoas.VentaModelAssembler;
import com.minimarket.service.VentaService;
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

@Tag(name = "Ventas", description = "Registro y consulta de ventas.")
@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService ventaService;
    private final VentaModelAssembler assembler;

    public VentaController(VentaService ventaService, VentaModelAssembler assembler) {
        this.ventaService = ventaService;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar ventas")
    @GetMapping
    public CollectionModel<EntityModel<Venta>> listarVentas() {
        List<EntityModel<Venta>> modelos = ventaService.findAll().stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos, linkTo(methodOn(VentaController.class).listarVentas()).withSelfRel());
    }

    @Operation(summary = "Buscar venta por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Venta encontrada"), @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Venta>> obtenerVentaPorId(@PathVariable Long id) {
        Venta venta = ventaService.findById(id);
        return venta == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(assembler.toModel(venta));
    }

    @Operation(summary = "Listar ventas de un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public CollectionModel<EntityModel<Venta>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<EntityModel<Venta>> modelos = ventaService.findByUsuarioId(usuarioId).stream().map(assembler::toModel).toList();
        return CollectionModel.of(modelos,
                linkTo(methodOn(VentaController.class).listarPorUsuario(usuarioId)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(usuarioId)).withRel("usuario"));
    }

    @Operation(summary = "Registrar venta")
    @PostMapping
    public ResponseEntity<EntityModel<Venta>> guardarVenta(@Valid @RequestBody Venta venta) {
        Venta guardada = ventaService.save(venta);
        URI location = linkTo(methodOn(VentaController.class).obtenerVentaPorId(guardada.getId())).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(guardada));
    }
}
