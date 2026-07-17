package com.minimarket.notificaciones.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
public record Notificacion(Long id, @NotBlank String tipo, @NotNull Long referenciaId,
                           @NotBlank String destinatario, Instant fecha) { }
