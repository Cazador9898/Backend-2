package com.minimarket.hateoas;

import com.minimarket.controller.InventarioController;
import com.minimarket.controller.ProductoController;
import com.minimarket.entity.Inventario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<Inventario, EntityModel<Inventario>> {
    @Override
    public EntityModel<Inventario> toModel(Inventario inventario) {
        EntityModel<Inventario> model = EntityModel.of(inventario,
                linkTo(methodOn(InventarioController.class).obtenerInventarioPorId(inventario.getId())).withSelfRel(),
                linkTo(methodOn(InventarioController.class).listarInventario()).withRel("inventario"));
        if (inventario.getProducto() != null && inventario.getProducto().getId() != null) {
            model.add(linkTo(methodOn(ProductoController.class)
                    .obtenerProductoPorId(inventario.getProducto().getId())).withRel("producto"));
            model.add(linkTo(methodOn(InventarioController.class)
                    .listarPorProducto(inventario.getProducto().getId())).withRel("historial-producto"));
        }
        return model;
    }
}
