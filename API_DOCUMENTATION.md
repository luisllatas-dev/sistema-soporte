# 🛠️ API de Gestión de Solicitudes de Soporte Técnico

Esta es la documentación oficial de la API REST para el sistema de solicitudes de soporte técnico. La API permite registrar, consultar, actualizar y eliminar solicitudes, clientes y técnicos, persistiendo la información en una base de datos relacional MySQL.

---

## 🚀 Información General

* **Tecnologías:**
  * Java 25
  * Spring Boot 4.0.6
  * Springdoc OpenAPI 2.8.8 (Swagger)
  * Persistencia: **Spring Data JPA** con **MySQL Database**.
* **Servidor Local:** `http://localhost:8080`
* **Documentación Interactiva:**
  * **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  * **OpenAPI Specs (JSON):** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 📐 Arquitectura de capas

```
Controller (@Valid DTO) → Service (DTO → Entity) → Repository (Entity) → MySQL
```

* **DTOs de entrada** (`*RequestDTO`): Contienen las validaciones de Spring (`@NotBlank`, `@Email`, `@NotNull`).
* **Entidades JPA** (`model/*`): Solo contienen anotaciones de persistencia (`@Entity`, `@Column`, `@ManyToOne`).
* **Entidades de salida**: Los endpoints GET retornan las entidades completas con relaciones resueltas.

---

## 📦 DTOs de Entrada (Request)

### ClienteRequestDTO
| Campo | Tipo | Validaciones | Descripción |
| :--- | :--- | :--- | :--- |
| `nombre` | String | `@NotBlank` | Nombre completo del cliente. |
| `correoElectronico` | String | `@NotBlank`, `@Email` | Correo electrónico de contacto. |

### TecnicoRequestDTO
| Campo | Tipo | Validaciones | Descripción |
| :--- | :--- | :--- | :--- |
| `nombre` | String | `@NotBlank` | Nombre completo del técnico. |
| `especialidad` | String | `@NotBlank` | Especialidad técnica. |

### SolicitudRequestDTO
| Campo | Tipo | Validaciones | Descripción |
| :--- | :--- | :--- | :--- |
| `descripcion` | String | `@NotBlank` | Descripción del problema. |
| `estado` | Enum | `@NotNull` | Estado: `ABIERTA`, `EN_PROCESO`, `CERRADA`. |
| `clienteId` | Long | `@NotNull` | ID del cliente existente en la BD. |
| `tecnicoAsignadoId` | Long | `@NotNull` | ID del técnico existente en la BD. |

---

## 📦 Modelos de Datos (Entidades JPA)

### Solicitud (`solicitudes`)
| Campo | Tipo | Mapeo BD | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | Long | `PRIMARY KEY AUTO_INCREMENT` | Identificador único. |
| `descripcion` | String | `VARCHAR(255) NOT NULL` | Descripción del problema. |
| `estado` | Enum | `VARCHAR(20) NOT NULL` | Estado almacenado como String. |
| `fechaCreacion` | LocalDateTime | `DATETIME NOT NULL` | Autogenerada por `@PrePersist`. |
| `fechaActualizacion` | LocalDateTime | `DATETIME NULL` | Autogenerada por `@PreUpdate`. |
| `cliente` | Cliente | `FK → clientes(id)` | Relación `@ManyToOne`. |
| `tecnicoAsignado` | Tecnico | `FK → tecnicos(id)` | Relación `@ManyToOne`. |

### Cliente (`clientes`)
| Campo | Tipo | Mapeo BD | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | Long | `PRIMARY KEY AUTO_INCREMENT` | Identificador único. |
| `nombre` | String | `VARCHAR(255) NOT NULL` | Nombre completo. |
| `correoElectronico` | String | `VARCHAR(255) NOT NULL UNIQUE` | Correo electrónico único. |
| `fechaCreacion` | LocalDateTime | `DATETIME NOT NULL` | Auditoría de creación. |
| `fechaActualizacion` | LocalDateTime | `DATETIME NULL` | Auditoría de actualización. |

### Técnico (`tecnicos`)
| Campo | Tipo | Mapeo BD | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | Long | `PRIMARY KEY AUTO_INCREMENT` | Identificador único. |
| `nombre` | String | `VARCHAR(255) NOT NULL` | Nombre completo. |
| `especialidad` | String | `VARCHAR(255) NOT NULL` | Especialidad técnica. |
| `fechaCreacion` | LocalDateTime | `DATETIME NOT NULL` | Auditoría de creación. |
| `fechaActualizacion` | LocalDateTime | `DATETIME NULL` | Auditoría de actualización. |

---

## 🛤️ Endpoints

### Clientes (`/api/clientes`)

#### 1. Listar / Buscar clientes por Nombre
* **Método:** `GET`
* **Ruta:** `/api/clientes`
* **Parámetros de Consulta (Opcional):**
  * `nombre` (String): Filtrar por coincidencia en el nombre (ej. `/api/clientes?nombre=Jose`).

#### 2. Buscar cliente por ID
* **Método:** `GET`
* **Ruta:** `/api/clientes/{id}`

#### 3. Crear un cliente
* **Método:** `POST`
* **Ruta:** `/api/clientes`

#### 4. Actualizar un cliente
* **Método:** `PUT`
* **Ruta:** `/api/clientes/{id}`

#### 5. Eliminar un cliente
* **Método:** `DELETE`
* **Ruta:** `/api/clientes/{id}`

---

### Técnicos (`/api/tecnicos`)

#### 1. Listar / Filtrar técnicos por Especialidad o Nombre
* **Método:** `GET`
* **Ruta:** `/api/tecnicos`
* **Parámetros de Consulta (Opcional):**
  * `especialidad` (String): Filtrar por especialidad (ej. `/api/tecnicos?especialidad=Redes`).
  * `nombre` (String): Filtrar por coincidencia en el nombre (ej. `/api/tecnicos?nombre=Ana`).

#### 2. Buscar técnico por ID
* **Método:** `GET`
* **Ruta:** `/api/tecnicos/{id}`

#### 3. Crear un técnico
* **Método:** `POST`
* **Ruta:** `/api/tecnicos`

#### 4. Actualizar un técnico
* **Método:** `PUT`
* **Ruta:** `/api/tecnicos/{id}`

#### 5. Eliminar un técnico
* **Método:** `DELETE`
* **Ruta:** `/api/tecnicos/{id}`

---

### Solicitudes (`/api/solicitudes`)

#### 1. Listar todas las solicitudes
* **Método:** `GET`
* **Ruta:** `/api/solicitudes`
* **Parámetros de Consulta (Opcional):**
  * `estado`: Filtrar por `ABIERTA`, `EN_PROCESO`, o `CERRADA`.

#### 2. Buscar solicitudes por Cliente
* **Método:** `GET`
* **Ruta:** `/api/solicitudes/cliente/{clienteId}`
* **Descripción:** Retorna el historial completo de solicitudes creadas por un cliente.

#### 3. Buscar solicitudes por Técnico
* **Método:** `GET`
* **Ruta:** `/api/solicitudes/tecnico/{tecnicoId}`
* **Descripción:** Retorna la lista de solicitudes asignadas a un técnico.

#### 4. Buscar solicitudes por texto de descripción
* **Método:** `GET`
* **Ruta:** `/api/solicitudes/buscar?texto={texto}`
* **Descripción:** Busca coincidencia de texto dentro de la descripción del problema (case-insensitive).

#### 5. Buscar solicitud por ID
* **Método:** `GET`
* **Ruta:** `/api/solicitudes/{id}`

#### 6. Crear una solicitud
* **Método:** `POST`
* **Ruta:** `/api/solicitudes`
* **Request Body:**
  ```json
  {
    "descripcion": "No hay conexión a internet en contabilidad",
    "estado": "ABIERTA",
    "clienteId": 1,
    "tecnicoAsignadoId": 1
  }
  ```

#### 7. Actualizar una solicitud
* **Método:** `PUT`
* **Ruta:** `/api/solicitudes/{id}`

#### 8. Actualizar estado de una solicitud
* **Método:** `PATCH`
* **Ruta:** `/api/solicitudes/{id}/estado/{estado}`

#### 9. Eliminar una solicitud
* **Método:** `DELETE`
* **Ruta:** `/api/solicitudes/{id}`
