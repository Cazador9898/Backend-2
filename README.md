# Minimarket Plus — EFT Semana 9

Solución backend compuesta por **dos microservicios ejecutables e integrados**:

1. **minimarket-core-service (puerto 8080):** catálogo, categorías, inventario, carrito, usuarios, ventas y detalles de venta.
2. **notificaciones-service (puerto 8081):** recibe y registra eventos generados cuando se concreta una venta.

## Requisitos implementados

- Arquitectura de microservicios con comunicación HTTP entre servicios.
- Autenticación JWT stateless.
- Autorización por roles: `ROLE_ADMIN`, `ROLE_CAJERO` y `ROLE_CLIENTE`.
- Contraseñas protegidas con BCrypt.
- API interna entre microservicios protegida mediante `X-API-KEY`.
- OpenAPI/Swagger y respuestas HATEOAS.
- Validaciones de entrada y manejo uniforme de errores.
- Pruebas unitarias, de seguridad y de integración con MockMvc.
- Reporte JaCoCo en cada servicio.
- Dockerfiles y `docker-compose.yml`.

## Ejecución local

Primero inicia notificaciones:

```bash
cd notificaciones-service
./mvnw spring-boot:run
```

Luego inicia el servicio principal:

```bash
cd minimarket-core-service
./mvnw spring-boot:run
```

También se puede compilar y ejecutar con Docker:

```bash
cd minimarket-core-service && ./mvnw clean package
cd ../notificaciones-service && ./mvnw clean package
cd ..
docker compose up --build
```

## Usuarios de demostración

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `Admin123!` | Administrador |
| `cajero` | `Cajero123!` | Cajero |
| `cliente` | `Cliente123!` | Cliente |

## Obtener token JWT

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "Admin123!"
}
```

Usar el token retornado en las solicitudes protegidas:

```http
Authorization: Bearer <TOKEN>
```

## Documentación

- Core Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Core OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Notificaciones Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- Notificaciones OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## Pruebas y cobertura

En cada microservicio:

```bash
./mvnw clean test
```

Reporte de cobertura:

```text
target/site/jacoco/index.html
```

## Flujo integrado de venta

1. Un cajero o administrador obtiene un JWT.
2. Registra una venta en `POST /api/ventas`.
3. El servicio principal valida productos y stock.
4. La operación descuenta stock dentro de una transacción.
5. Se guarda la venta y sus detalles.
6. El servicio principal envía un evento al microservicio de notificaciones.
7. La notificación puede consultarse en `GET http://localhost:8081/api/notificaciones`.
