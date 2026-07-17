package com.minimarket.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Map;

@Component
public class NotificacionClient {
    private static final Logger log = LoggerFactory.getLogger(NotificacionClient.class);
    private final RestClient restClient;
    private final String apiKey;

    public NotificacionClient(RestClient.Builder builder,
                              @Value("${services.notificaciones.url:http://localhost:8081}") String baseUrl,
                              @Value("${services.notificaciones.api-key:clave-interna-minimarket}") String apiKey) {
        this.restClient = builder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    public void notificarVenta(Long ventaId, String usuario) {
        try {
            restClient.post().uri("/api/notificaciones")
                    .header("X-API-KEY", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("tipo", "VENTA_REGISTRADA", "referenciaId", ventaId,
                            "destinatario", usuario, "fecha", Instant.now().toString()))
                    .retrieve().toBodilessEntity();
        } catch (Exception ex) {
            log.warn("La venta fue guardada, pero el microservicio de notificaciones no respondió: {}", ex.getMessage());
        }
    }
}
