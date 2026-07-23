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

### ApiResponse (Envoltura Genérica)
| Campo | Tipo | Descripción |
| :--- | :--- | :--- |
| `success` | boolean | `true` si la operación fue exitosa. |
| `message` | String | Descripción del resultado. |
| `data` | T (Genérico) | Payload de respuesta (`null` en errores). |
| `errors` | Object | Mapa de errores de validación. |
| `timestamp` | LocalDateTime | Estampa de tiempo del servidor. |

---

## 🛤️ Endpoints

### Clientes (`/api/clientes`)

#### 1. Listar todos los clientes
* **Método:** `GET`
* **Ruta:** `/api/clientes`

#### 2. Buscar cliente por ID
* **Método:** `GET`
* **Ruta:** `/api/clientes/{id}`

#### 3. Crear un cliente
* **Método:** `POST`
* **Ruta:** `/api/clientes`
* **Request Body:**
  ```json
  {
    "nombre": "Carlos Mendoza",
    "correoElectronico": "carlos@email.com"
  }
  ```
* **Respuesta (201 Created):**
  ```json
  {
    "success": true,
    "message": "Cliente creado exitosamente",
    "data": {
      "id": 1,
      "nombre": "Carlos Mendoza",
      "correoElectronico": "carlos@email.com",
      "fechaCreacion": "2026-07-22T23:30:00",
      "fechaActualizacion": null
    }
  }
  ```

#### 4. Actualizar un cliente
* **Método:** `PUT`
* **Ruta:** `/api/clientes/{id}`
* **Request Body:** Igual que POST.

#### 5. Eliminar un cliente
* **Método:** `DELETE`
* **Ruta:** `/api/clientes/{id}`

---

### Técnicos (`/api/tecnicos`)

#### 1. Listar todos los técnicos
* **Método:** `GET`
* **Ruta:** `/api/tecnicos`

#### 2. Buscar técnico por ID
* **Método:** `GET`
* **Ruta:** `/api/tecnicos/{id}`

#### 3. Crear un técnico
* **Método:** `POST`
* **Ruta:** `/api/tecnicos`
* **Request Body:**
  ```json
  {
    "nombre": "Ana García",
    "especialidad": "Redes y Conectividad"
  }
  ```
* **Respuesta (201 Created):**
  ```json
  {
    "success": true,
    "message": "Técnico creado exitosamente",
    "data": {
      "id": 1,
      "nombre": "Ana García",
      "especialidad": "Redes y Conectividad",
      "fechaCreacion": "2026-07-22T23:30:00",
      "fechaActualizacion": null
    }
  }
  ```

#### 4. Actualizar un técnico
* **Método:** `PUT`
* **Ruta:** `/api/tecnicos/{id}`
* **Request Body:** Igual que POST.

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

#### 2. Buscar solicitud por ID
* **Método:** `GET`
* **Ruta:** `/api/solicitudes/{id}`
* **Respuesta (200 OK):**
  ```json
  {
    "success": true,
    "message": "Solicitud encontrada exitosamente",
    "data": {
      "id": 1,
      "descripcion": "No hay conexión a internet",
      "estado": "ABIERTA",
      "fechaCreacion": "2026-07-22T23:30:00",
      "fechaActualizacion": null,
      "cliente": {
        "id": 1,
        "nombre": "Carlos Mendoza",
        "correoElectronico": "carlos@email.com",
        "fechaCreacion": "2026-07-22T23:28:00",
        "fechaActualizacion": null
      },
      "tecnicoAsignado": {
        "id": 1,
        "nombre": "Ana García",
        "especialidad": "Redes y Conectividad",
        "fechaCreacion": "2026-07-22T23:29:00",
        "fechaActualizacion": null
      }
    }
  }
  ```

#### 3. Crear una solicitud
* **Método:** `POST`
* **Ruta:** `/api/solicitudes`
* **Request Body (solo IDs):**
  ```json
  {
    "descripcion": "No hay conexión a internet en contabilidad",
    "estado": "ABIERTA",
    "clienteId": 1,
    "tecnicoAsignadoId": 1
  }
  ```
  > **Nota:** El cliente y técnico deben existir previamente. Si el ID no existe, se retorna 404.

* **Respuesta (201 Created):**
  ```json
  {
    "success": true,
    "message": "Solicitud creada exitosamente",
    "data": {
      "id": 1,
      "descripcion": "No hay conexión a internet en contabilidad",
      "estado": "ABIERTA",
      "fechaCreacion": "2026-07-22T23:30:00",
      "fechaActualizacion": null,
      "cliente": {
        "id": 1,
        "nombre": "Carlos Mendoza",
        "correoElectronico": "carlos@email.com"
      },
      "tecnicoAsignado": {
        "id": 1,
        "nombre": "Ana García",
        "especialidad": "Redes y Conectividad"
      }
    }
  }
  ```

* **Error si cliente/técnico no existe (404):**
  ```json
  {
    "success": false,
    "message": "No se encontró el cliente con ID: 99"
  }
  ```

#### 4. Actualizar una solicitud
* **Método:** `PUT`
* **Ruta:** `/api/solicitudes/{id}`
* **Request Body:** Igual que POST (con `clienteId` y `tecnicoAsignadoId`).

#### 5. Actualizar estado de una solicitud
* **Método:** `PATCH`
* **Ruta:** `/api/solicitudes/{id}/estado/{estado}`
* **Ejemplo:** `PATCH /api/solicitudes/1/estado/EN_PROCESO`

#### 6. Eliminar una solicitud
* **Método:** `DELETE`
* **Ruta:** `/api/solicitudes/{id}`

---

## ⚠️ Manejo de Errores Globales

* **400 Bad Request:** Validaciones fallidas en los DTOs (`@NotBlank`, `@Email`, `@NotNull`).
  ```json
  {
    "success": false,
    "message": "Los datos enviados contienen errores",
    "errors": {
      "descripcion": "La descripción de la solicitud es obligatoria",
      "clienteId": "El ID del cliente es obligatorio"
    }
  }
  ```
* **404 Not Found:** Recurso no encontrado (solicitud, cliente o técnico).
* **500 Internal Server Error:** Fallos inesperados del servidor.
