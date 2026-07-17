package com.minimarket.service.impl;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.integration.NotificacionClient;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.VentaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final NotificacionClient notificacionClient;

    public VentaServiceImpl(VentaRepository ventaRepository, ProductoRepository productoRepository,
                            NotificacionClient notificacionClient) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.notificacionClient = notificacionClient;
    }

    @Override
    public List<Venta> findAll() { return ventaRepository.findAll(); }

    @Override
    public Venta findById(Long id) { return ventaRepository.findById(id).orElse(null); }

    @Override
    @Transactional
    public Venta save(Venta venta) {
        if (venta.getUsuario() == null || venta.getUsuario().getId() == null) {
            throw new IllegalArgumentException("La venta debe indicar un usuario válido");
        }
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe contener al menos un producto");
        }
        venta.setFecha(venta.getFecha() == null ? new Date() : venta.getFecha());
        for (DetalleVenta detalle : venta.getDetalles()) {
            if (detalle.getProducto() == null || detalle.getProducto().getId() == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException("Cada detalle debe indicar producto y cantidad positiva");
            }
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + detalle.getProducto().getId()));
            if (producto.getStock() < detalle.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
            detalle.setProducto(producto);
            detalle.setPrecio(producto.getPrecio());
            detalle.setVenta(venta);
        }
        Venta saved = ventaRepository.save(venta);
        notificacionClient.notificarVenta(saved.getId(), saved.getUsuario().getUsername() == null ? "usuario" : saved.getUsuario().getUsername());
        return saved;
    }

    @Override
    public List<Venta> findByUsuarioId(Long usuarioId) { return ventaRepository.findByUsuarioId(usuarioId); }
}
