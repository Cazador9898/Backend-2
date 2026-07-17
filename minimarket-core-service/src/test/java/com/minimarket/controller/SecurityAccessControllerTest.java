package com.minimarket.controller;

import com.minimarket.hateoas.InventarioModelAssembler;
import com.minimarket.hateoas.ProductoModelAssembler;
import com.minimarket.hateoas.VentaModelAssembler;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.filter.JwtAuthenticationFilter;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.InventarioService;
import com.minimarket.service.ProductoService;
import com.minimarket.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        ProductoController.class,
        InventarioController.class,
        VentaController.class,
        HolaMundoController.class
})
@Import({
        SecurityConfig.class,
        SecurityAccessControllerTest.TestSecurityConfig.class
})
class SecurityAccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private InventarioService inventarioService;

    @MockBean
    private VentaService ventaService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private ProductoModelAssembler productoAssembler;

    @MockBean
    private InventarioModelAssembler inventarioAssembler;

    @MockBean
    private VentaModelAssembler ventaAssembler;

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        JwtAuthenticationFilter jwtAuthenticationFilter(
                CustomUserDetailsService customUserDetailsService) {

            return new JwtAuthenticationFilter(null, customUserDetailsService) {

                @Override
                protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain)
                        throws ServletException, IOException {

                    filterChain.doFilter(request, response);
                }
            };
        }
    }

    @Test
    void publicHola_sinAutenticacion_retornaOk() throws Exception {
        mockMvc.perform(get("/public/hola"))
                .andExpect(status().isOk());
    }

    @Test
    void endpointPrivado_sinToken_retornaUnauthorized() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CAJERO")
    void actualizarProducto_cajero_retornaForbidden() throws Exception {
        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Arroz",
                                  "precio": 1290,
                                  "stock": 10
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void registrarInventario_cliente_retornaForbidden() throws Exception {
        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cantidad": 5,
                                  "tipoMovimiento": "SALIDA",
                                  "fechaMovimiento": "2026-07-16T12:00:00Z"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void generarVenta_cliente_retornaForbidden() throws Exception {
        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }
}