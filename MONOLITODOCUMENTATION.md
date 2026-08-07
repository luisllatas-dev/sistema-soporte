# 📖 Especificación Técnica: Monolito de Soporte Técnico (`solicitudes`)

---

## 1. Visión General y Arquitectura

El **Monolito `solicitudes`** maneja la lógica de negocio central del sistema: registro de tickets de soporte técnico, gestión de clientes, técnicos asignados y ciclo de vida de los estados.

Implementa **Security-by-Design** mediante filtros de Spring Security y un interceptor que valida el token JWT emitido por el `auth-service` usando la clave secreta compartida.

```
                      +-------------------+
                      |   Cliente Frontend |
                      +---------+---------+
                                |
                                | Authorization: Bearer <JWT>
                                v
                      +-------------------+
                      | JwtValidationSvc  | (Verifica firma HMAC SHA-256)
                      +---------+---------+
                                |
                                v
                      +-------------------+
                      | SolicitudController| (Aplica filtros por Rol)
                      +---------+---------+
                                |
                                v
                      +-------------------+
                      |  MySQL (solicitudes)|
                      +-------------------+
```

---

## 2. Modelo de Datos (`solicitudes`)

### Tabla: `solicitudes`

| Columna | Tipo | Restricciones | Descripción |
|---|---|---|---|
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Identificador único del ticket |
| `descripcion` | `VARCHAR(255)` | `NOT NULL` | Descripción del problema reportado |
| `estado` | `VARCHAR(20)` | `NOT NULL` | Estado: `ABIERTA`, `EN_PROCESO`, `CERRADA` |
| `observaciones` | `VARCHAR(500)` | `NULLABLE` | Notas del técnico sobre el trabajo realizado |
| `fecha_creacion` | `DATETIME` | `NOT NULL` | Timestamp de creación |
| `fecha_actualizacion` | `DATETIME` | `NULLABLE` | Timestamp de última edición |
| `cliente_id` | `BIGINT` | `FOREIGN KEY` (clientes) | Cliente que solicitó el servicio |
| `tecnico_id` | `BIGINT` | `FOREIGN KEY` (tecnicos), `NULLABLE` | Técnico asignado a atender |

---

## 3. Endpoints Filtrados por JWT (Security-by-Design)

### 3.1. Obtener Solicitudes (`GET /api/solicitudes`)
- **Comportamiento dinámico según el Token JWT:**
  - `ROLE_ADMINISTRADOR`: Retorna la totalidad de solicitudes registradas.
  - `ROLE_CLIENTE`: Retorna únicamente las solicitudes pertenecientes al email del cliente autenticado.
  - `ROLE_TECNICO`: Retorna únicamente las solicitudes asignadas al email del técnico autenticado.

### 3.2. Mis Solicitudes (`GET /api/solicitudes/mis-solicitudes`)
- Endpoint dedicado para consulta directa de clientes.

### 3.3. Mis Asignaciones (`GET /api/solicitudes/mis-asignaciones`)
- Endpoint dedicado para consulta directa de técnicos.

### 3.4. Actualizar Solicitud (`PUT /api/solicitudes/{id}`)
- Recibe el DTO con `descripcion`, `estado`, `clienteId`, `tecnicoAsignadoId` y **`observaciones`**.
- Restringe edición a Clientes si el estado ya no es `ABIERTA`.
