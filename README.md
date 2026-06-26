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



## Entrega
1.	Diseño y justificación de las pruebas unitarias
Las pruebas unitarias fueron diseñadas para validar las entidades clave del sistema Minimarket Plus: Producto, Inventario, Venta y Usuario. El objetivo fue comprobar que cada componente funcione correctamente y que las operaciones protegidas respeten los roles definidos en el sistema.
En la entidad Producto, las pruebas verifican que solo un usuario con rol ADMIN pueda modificar los datos de un producto. 
En Inventario, las pruebas validan el registro de movimientos de entrada y salida de stock. Se consideraron escenarios exitosos y de error para asegurar que solo usuarios autorizados puedan modificar existencias, evitando inconsistencias en el control de productos.
En Venta, las pruebas comprueban que solo usuarios con rol CAJERO puedan generar ventas. Además, se valida que la venta refleje correctamente los productos vendidos, cantidades y precios, asegurando que la lógica de negocio se mantenga consistente.
En Usuario, las pruebas verifican intentos de autenticación válidos y no válidos. Esto permite comprobar que el sistema permita el acceso a usuarios correctos y rechace credenciales inválidas, fortaleciendo la seguridad del backend.
Estas pruebas fueron justificadas porque cubren los principales escenarios funcionales y de seguridad solicitados para el sistema, permitiendo validar tanto el comportamiento esperado como los casos de acceso denegado.
 2.	Descripción paso a paso de la configuración del entorno de pruebas
Para configurar el entorno de pruebas unitarias se utilizó Maven como herramienta de gestión del proyecto. Primero, se revisó la estructura del backend para asegurar que el código principal estuviera ubicado en src/main/java y las pruebas unitarias en src/test/java. Luego, se configuró el archivo pom.xml incorporando las dependencias necesarias para ejecutar pruebas con JUnit, Mockito y Spring Boot Test. Además, se agregó Spring Security Test para validar escenarios de autenticación y autorización según los roles definidos en el sistema, se configuró JaCoCo en el pom.xml para generar reportes de cobertura de pruebas. Esta herramienta permite revisar qué porcentaje del código fue ejecutado durante las pruebas unitarias. También se corrigió la codificación del archivo application.properties a UTF-8 para evitar errores durante la compilación. Además, se eliminó Lombok para evitar problemas de compatibilidad con la versión de Java utilizada, reemplazándolo por constructores, getters y setters tradicionales.
Finalmente, las pruebas se ejecutaron desde la terminal mediante el comando:
mvn clean test












3.	Resultados obtenidos y análisis de cobertura
 
 
 
-------------------------------------------------------------------------------
Test set: com.minimarket.controller.SecurityAccessControllerTest
-------------------------------------------------------------------------------
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.868 s -- in 
com.minimarket.controller.SecurityAccessControllerTest
-------------------------------------------------------------------------------
Test set: com.minimarket.MinimarketApplicationTests
-------------------------------------------------------------------------------
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.915 s -- in com.minimarket.MinimarketApplicationTests
 4.	 Explicación de cómo las pruebas contribuyen a la calidad del sistema
Las pruebas unitarias mejoran la calidad del sistema al verificar que cada componente funcione correctamente antes de su integración. Permitieron validar las operaciones de Producto, Inventario, Venta y Usuario, comprobando tanto escenarios exitosos como accesos no autorizados. Además, las pruebas ayudaron a detectar y corregir errores de configuración y seguridad, aumentando la estabilidad y confiabilidad del backend. Finalmente, la cobertura obtenida con JaCoCo permitió identificar las partes del código que fueron evaluadas y aquellas que pueden mejorarse en futuras versiones.

5.	Recomendaciones para mejorar la implementación
Se recomienda ampliar las pruebas unitarias incorporando más casos límite, como productos sin stock, ventas con cantidades inválidas e intentos de acceso con roles incorrectos. También se sugiere mejorar las reglas de seguridad agregando validaciones más específicas por endpoint y mantener actualizadas las dependencias del proyecto para evitar problemas de compatibilidad. Finalmente, es recomendable revisar periódicamente la cobertura con JaCoCo, para identificar clases o métodos que necesiten nuevas pruebas.