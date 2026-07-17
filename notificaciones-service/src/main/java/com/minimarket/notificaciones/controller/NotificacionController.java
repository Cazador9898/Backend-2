package com.minimarket.notificaciones.controller;

import com.minimarket.notificaciones.model.Notificacion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name="Notificaciones", description="Recepción de eventos generados por otros microservicios")
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
 private final List<Notificacion> store = new CopyOnWriteArrayList<>();
 private final AtomicLong sequence = new AtomicLong();

 @Operation(summary="Listar notificaciones procesadas")
 @GetMapping
 public CollectionModel<EntityModel<Notificacion>> listar() {
  List<EntityModel<Notificacion>> items = store.stream().map(this::model).toList();
  return CollectionModel.of(items, linkTo(methodOn(NotificacionController.class).listar()).withSelfRel());
 }

 @Operation(summary="Recibir una notificación interna")
 @PostMapping
 public ResponseEntity<EntityModel<Notificacion>> crear(@Valid @RequestBody Notificacion request) {
  Notificacion saved = new Notificacion(sequence.incrementAndGet(), request.tipo(), request.referenciaId(),
          request.destinatario(), request.fecha() == null ? Instant.now() : request.fecha());
  store.add(saved);
  return ResponseEntity.created(URI.create("/api/notificaciones/" + saved.id())).body(model(saved));
 }

 @Operation(summary="Consultar una notificación")
 @GetMapping("/{id}")
 public ResponseEntity<EntityModel<Notificacion>> obtener(@PathVariable Long id) {
  return store.stream().filter(n -> n.id().equals(id)).findFirst().map(n -> ResponseEntity.ok(model(n)))
          .orElseGet(() -> ResponseEntity.notFound().build());
 }
 private EntityModel<Notificacion> model(Notificacion n) {
  return EntityModel.of(n, linkTo(methodOn(NotificacionController.class).obtener(n.id())).withSelfRel(),
          linkTo(methodOn(NotificacionController.class).listar()).withRel("notificaciones"));
 }
}
