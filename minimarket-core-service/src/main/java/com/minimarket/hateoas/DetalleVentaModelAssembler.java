package com.minimarket.hateoas;

import com.minimarket.controller.DetalleVentaController;
import com.minimarket.controller.ProductoController;
import com.minimarket.controller.VentaController;
import com.minimarket.entity.DetalleVenta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DetalleVentaModelAssembler implements RepresentationModelAssembler<DetalleVenta, EntityModel<DetalleVenta>> {
    @Override
    public EntityModel<DetalleVenta> toModel(DetalleVenta detalle) {
        EntityModel<DetalleVenta> model = EntityModel.of(detalle,
                linkTo(methodOn(DetalleVentaController.class).obtenerDetallePorId(detalle.getId())).withSelfRel(),
                linkTo(methodOn(DetalleVentaController.class).listarDetalles()).withRel("detalles-venta"));
        if (detalle.getVenta() != null && detalle.getVenta().getId() != null) {
            model.add(linkTo(methodOn(VentaController.class)
                    .obtenerVentaPorId(detalle.getVenta().getId())).withRel("venta"));
        }
        if (detalle.getProducto() != null && detalle.getProducto().getId() != null) {
            model.add(linkTo(methodOn(ProductoController.class)
                    .obtenerProductoPorId(detalle.getProducto().getId())).withRel("producto"));
        }
        return model;
    }
}
