package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.integration.NotificacionClient;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {
    @Mock VentaRepository ventaRepository;
    @Mock ProductoRepository productoRepository;
    @Mock NotificacionClient notificacionClient;
    @InjectMocks VentaServiceImpl ventaService;

    @Test
    void save_ventaValida_descuentaStockYNotifica() {
        Producto producto = new Producto(); producto.setId(1L); producto.setNombre("Pan"); producto.setPrecio(1000.0); producto.setStock(10);
        DetalleVenta detalle = new DetalleVenta(); detalle.setProducto(producto); detalle.setCantidad(2);
        Usuario usuario = new Usuario(); usuario.setId(1L); usuario.setUsername("cajero");
        Venta venta = new Venta(); venta.setUsuario(usuario); venta.setDetalles(List.of(detalle));
        Venta guardada = venta; guardada.setId(5L);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(ventaRepository.save(venta)).thenReturn(guardada);

        Venta resultado = ventaService.save(venta);

        assertEquals(8, producto.getStock());
        assertEquals(1000.0, resultado.getDetalles().get(0).getPrecio());
        verify(productoRepository).save(producto);
        verify(notificacionClient).notificarVenta(5L, "cajero");
    }

    @Test
    void save_stockInsuficiente_lanzaExcepcion() {
        Producto producto = new Producto(); producto.setId(1L); producto.setNombre("Pan"); producto.setPrecio(1000.0); producto.setStock(1);
        DetalleVenta detalle = new DetalleVenta(); detalle.setProducto(producto); detalle.setCantidad(2);
        Usuario usuario = new Usuario(); usuario.setId(1L);
        Venta venta = new Venta(); venta.setUsuario(usuario); venta.setDetalles(List.of(detalle));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        assertThrows(IllegalArgumentException.class, () -> ventaService.save(venta));
        verify(ventaRepository, never()).save(any());
    }

    @Test void findByUsuarioId_retornaVentasDelUsuario() {
        when(ventaRepository.findByUsuarioId(3L)).thenReturn(List.of(new Venta(), new Venta()));
        assertEquals(2, ventaService.findByUsuarioId(3L).size());
    }

    @Test void findById_ventaNoExiste_retornaNull() {
        when(ventaRepository.findById(10L)).thenReturn(Optional.empty());
        assertNull(ventaService.findById(10L));
    }
}
