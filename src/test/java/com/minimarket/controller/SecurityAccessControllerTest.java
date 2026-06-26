package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.InventarioService;
import com.minimarket.service.ProductoService;
import com.minimarket.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ProductoController.class, InventarioController.class, VentaController.class, HolaMundoController.class})
@Import(SecurityConfig.class)
class SecurityAccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private InventarioService inventarioService;

    @MockBean
    private VentaService ventaService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void publicHola_sinAutenticacion_retornaOk() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/public/hola"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizarProducto_usuarioAdmin_retornaOk() throws Exception {
        Producto existente = new Producto();
        existente.setId(1L);
        existente.setNombre("Arroz");
        existente.setPrecio(1290.0);
        existente.setStock(20);

        Producto actualizado = new Producto();
        actualizado.setNombre("Arroz integral");
        actualizado.setPrecio(1590.0);
        actualizado.setStock(30);

        when(productoService.findById(1L)).thenReturn(existente);
        when(productoService.save(any(Producto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CAJERO")
    void actualizarProducto_usuarioCajero_retornaForbidden() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Arroz");
        producto.setPrecio(1290.0);
        producto.setStock(10);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void registrarMovimientoInventario_usuarioAdmin_retornaOk() throws Exception {
        Inventario inventario = new Inventario();
        inventario.setCantidad(5);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date());

        when(inventarioService.save(any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void registrarMovimientoInventario_usuarioCliente_retornaForbidden() throws Exception {
        Inventario inventario = new Inventario();
        inventario.setCantidad(5);
        inventario.setTipoMovimiento("Salida");
        inventario.setFechaMovimiento(new Date());

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CAJERO")
    void generarVenta_usuarioCajero_retornaOk() throws Exception {
        Venta venta = new Venta();
        venta.setFecha(new Date());

        when(ventaService.save(any(Venta.class))).thenReturn(venta);

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void generarVenta_usuarioCliente_retornaForbidden() throws Exception {
        Venta venta = new Venta();
        venta.setFecha(new Date());

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isForbidden());
    }
}
