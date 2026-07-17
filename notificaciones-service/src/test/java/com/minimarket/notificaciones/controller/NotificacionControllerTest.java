package com.minimarket.notificaciones.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class NotificacionControllerTest {
 @Autowired MockMvc mvc; @Autowired ObjectMapper mapper;
 @Test void apiKeyCorrectaCreaNotificacion() throws Exception {
  mvc.perform(post("/api/notificaciones").header("X-API-KEY","clave-interna-minimarket")
   .contentType(MediaType.APPLICATION_JSON).content("{\"tipo\":\"VENTA_REGISTRADA\",\"referenciaId\":1,\"destinatario\":\"cliente\"}"))
   .andExpect(status().isCreated());
 }
 @Test void sinApiKeyEsRechazado() throws Exception { mvc.perform(post("/api/notificaciones").contentType(MediaType.APPLICATION_JSON).content("{}" )).andExpect(status().isUnauthorized()); }
}
