package com.minimarket.service;

import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.ProductoServiceImpl;
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
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void save_productoValido_guardaProducto() {
        Producto producto = new Producto();
        producto.setNombre("Arroz");
        producto.setPrecio(1290.0);
        producto.setStock(15);

        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertEquals("Arroz", resultado.getNombre());
        assertEquals(1290.0, resultado.getPrecio());
        assertEquals(15, resultado.getStock());
        verify(productoRepository).save(producto);
    }

    @Test
    void findById_productoExiste_retornaProducto() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Leche");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Leche", resultado.getNombre());
    }

    @Test
    void findById_productoNoExiste_retornaNull() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(productoService.findById(99L));
    }

    @Test
    void findByCategoriaId_retornaProductosDeCategoria() {
        when(productoRepository.findByCategoriaId(2L)).thenReturn(List.of(new Producto(), new Producto()));

        assertEquals(2, productoService.findByCategoriaId(2L).size());
    }
}
