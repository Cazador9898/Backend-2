package com.minimarket.service;

import com.minimarket.entity.Inventario;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.service.impl.InventarioServiceImpl;
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
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    @Test
    void save_movimientoEntrada_guardaMovimiento() {
        Inventario inventario = new Inventario();
        inventario.setTipoMovimiento("Entrada");
        inventario.setCantidad(10);
        inventario.setFechaMovimiento(new Date());

        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioService.save(inventario);

        assertEquals("Entrada", resultado.getTipoMovimiento());
        assertEquals(10, resultado.getCantidad());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void findByProductoId_retornaMovimientosDelProducto() {
        when(inventarioRepository.findByProductoId(1L)).thenReturn(List.of(new Inventario()));

        assertEquals(1, inventarioService.findByProductoId(1L).size());
    }

    @Test
    void findById_movimientoNoExiste_retornaNull() {
        when(inventarioRepository.findById(50L)).thenReturn(Optional.empty());

        assertNull(inventarioService.findById(50L));
    }
}
