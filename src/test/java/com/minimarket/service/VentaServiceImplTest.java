package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    @Test
    void save_ventaConDetalle_guardaVenta() {
        Producto producto = new Producto();
        producto.setNombre("Pan");
        producto.setPrecio(1000.0);

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        detalle.setPrecio(1000.0);

        Venta venta = new Venta();
        venta.setFecha(new Date());
        venta.setDetalles(List.of(detalle));

        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.save(venta);

        assertEquals(1, resultado.getDetalles().size());
        assertEquals(2, resultado.getDetalles().get(0).getCantidad());
        verify(ventaRepository).save(venta);
    }

    @Test
    void findByUsuarioId_retornaVentasDelUsuario() {
        when(ventaRepository.findByUsuarioId(3L)).thenReturn(List.of(new Venta(), new Venta()));

        assertEquals(2, ventaService.findByUsuarioId(3L).size());
    }

    @Test
    void findById_ventaNoExiste_retornaNull() {
        when(ventaRepository.findById(10L)).thenReturn(Optional.empty());

        assertNull(ventaService.findById(10L));
    }
}
