# Minimarket Plus - Semana 6

Código preparado para ejecutar pruebas unitarias y de seguridad con JUnit, Mockito, Spring Security Test y JaCoCo.

## Comandos

```bash
mvn clean test
```

Reporte de cobertura JaCoCo:

```bash
target/site/jacoco/index.html
```

## Pruebas incluidas

- Producto: modificación permitida solo para rol ADMIN.
- Inventario: registro de movimientos permitido solo para rol ADMIN.
- Venta: generación permitida solo para rol CAJERO.
- Usuario/autenticación: carga de usuario válido, usuario inválido y roles Spring Security.
- Servicios: pruebas unitarias con Mockito para Producto, Inventario y Venta.
