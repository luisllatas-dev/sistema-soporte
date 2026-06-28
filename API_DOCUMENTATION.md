# 🛠️ API de Gestión de Solicitudes de Soporte Técnico

Esta es la documentación oficial de la API REST para el sistema de solicitudes de soporte técnico. La API permite registrar, consultar, actualizar y eliminar solicitudes, asignando clientes y técnicos responsables, persistiendo la información directamente en una base de datos relacional.

---

## 🚀 Información General

* **Tecnologías:**
  * Java 25
  * Spring Boot 4.0.6
  * Springdoc OpenAPI 2.8.8 (Swagger)
  * Persistencia: **Spring Data JPA** (`IClienteRepository`, `ITecnicoRepository`, `ISolicitudRepository`) con **MySQL Database**.
* **Servidor Local:** `http://localhost:8080`
* **Documentación Interactiva:**
  * **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  * **OpenAPI Specs (JSON):** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 📦 Modelos de Datos (Entidades JPA)

### 1. Solicitud (`solicitudes`)
Representa una solicitud de soporte técnico en el sistema.

| Campo | Tipo | Mapeo BD / Validaciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | Long | `PRIMARY KEY AUTO_INCREMENT` | Identificador único autogenerado en la base de datos. |
| `descripcion` | String | `VARCHAR(255) NOT NULL`, `@NotBlank` | Descripción detallada del problema. |
| `estado` | Enum | `VARCHAR(20) NOT NULL`, `@NotNull` | Estado actual (`ABIERTA`, `EN_PROCESO`, `CERRADA`). Se almacena como String. |
| `fechaCreacion` | LocalDateTime | `DATETIME NOT NULL`, `updatable = false` | Fecha y hora en la que se creó la solicitud (autogenerada por `@PrePersist`). |
| `fechaActualizacion` | LocalDateTime | `DATETIME NULL` | Fecha y hora de la última modificación (autogenerada por `@PreUpdate`). |
| `cliente` | Cliente | `@ManyToOne`, `@JoinColumn(name = "cliente_id")` | Cliente que solicita el soporte. Relación requerida (`@NotNull`, `@Valid`). |
| `tecnicoAsignado` | Tecnico | `@ManyToOne`, `@JoinColumn(name = "tecnico_id")` | Técnico asignado a la solicitud. Relación requerida (`@NotNull`, `@Valid`). |

### 2. Cliente (`clientes`)
Datos del cliente que reporta el problema.

| Campo | Tipo | Mapeo BD / Validaciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | Long | `PRIMARY KEY AUTO_INCREMENT` | Identificador único del cliente. |
| `nombre` | String | `VARCHAR(255) NOT NULL`, `@NotBlank` | Nombre completo del cliente. |
| `correoElectronico` | String | `VARCHAR(255) NOT NULL UNIQUE`, `@Email` | Correo electrónico de contacto único. |
| `fechaCreacion` | LocalDateTime | `DATETIME NOT NULL`, `updatable = false` | Registro de auditoría de creación. |
| `fechaActualizacion` | LocalDateTime | `DATETIME NULL` | Registro de auditoría de actualizaciones. |

### 3. Técnico (`tecnicos`)
Datos del especialista asignado a resolver el problema.

| Campo | Tipo | Mapeo BD / Validaciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | Long | `PRIMARY KEY AUTO_INCREMENT` | Identificador único del técnico. |
| `nombre` | String | `VARCHAR(255) NOT NULL`, `@NotBlank` | Nombre completo del técnico. |
| `especialidad` | String | `VARCHAR(255) NOT NULL`, `@NotBlank` | Especialidad técnica (ej. "Hardware", "Redes"). |
| `fechaCreacion` | LocalDateTime | `DATETIME NOT NULL`, `updatable = false` | Registro de auditoría de creación. |
| `fechaActualizacion` | LocalDateTime | `DATETIME NULL` | Registro de auditoría de actualizaciones. |

### 4. ApiResponse (Envoltura Genérica)
Estructura unificada de comunicación para todas las respuestas de la API (éxito y errores).

| Campo | Tipo | Descripción |
| :--- | :--- | :--- |
| `success` | boolean | `true` si la operación fue exitosa, `false` en caso de error. |
| `message` | String | Descripción explicativa del resultado de la operación. |
| `data` | T (Genérico) | Payload o datos devueltos por la API (será `null` en errores). |
| `errors` | Object | Mapa de detalles de error (usado para validaciones de campo `@Valid`). |
| `timestamp` | LocalDateTime | Estampa de tiempo de la respuesta del servidor. |

---

## 🛤️ Endpoints del API (`/api/solicitudes`)

### 1. Obtener todas las solicitudes
Retorna una lista completa de todas las solicitudes de soporte técnico guardadas en la base de datos MySQL, permitiendo filtrado por estado.

* **Método:** `GET`
* **Ruta:** `/api/solicitudes`
* **Parámetros de Consulta (Opcional):**
  * `estado` (Enum): Filtrar por `ABIERTA`, `EN_PROCESO`, o `CERRADA`. (ej. `/api/solicitudes?estado=ABIERTA`)
* **Respuesta Exitosa (200 OK):**
  ```json
  {
    "success": true,
    "message": "Lista de solicitudes obtenida exitosamente",
    "data": [
      {
        "id": 1,
        "descripcion": "No hay conexión a internet en el área de contabilidad",
        "estado": "ABIERTA",
        "fechaCreacion": "2026-06-27T16:55:19.177456103",
        "fechaActualizacion": null,
        "cliente": {
          "id": 1,
          "nombre": "Carlos Mendoza",
          "correoElectronico": "carlos.mendoza@email.com",
          "fechaCreacion": null,
          "fechaActualizacion": null
        },
        "tecnicoAsignado": {
          "id": 1,
          "nombre": "Ana García",
          "especialidad": "Redes y Conectividad",
          "fechaCreacion": null,
          "fechaActualizacion": null
        }
      }
    ],
    "timestamp": "2026-06-27T16:56:10.0040095"
  }
  ```

---

### 2. Buscar solicitud por ID
Busca una única solicitud en la base de datos por su ID único.

* **Método:** `GET`
* **Ruta:** `/api/solicitudes/{id}`
* **Parámetros de Ruta:**
  * `id` (Long): Identificador de la solicitud.
* **Respuesta Exitosa (200 OK):**
  ```json
  {
    "success": true,
    "message": "Solicitud encontrada exitosamente",
    "data": {
      "id": 1,
      "descripcion": "No hay conexión a internet en el área de contabilidad",
      "estado": "ABIERTA",
      "fechaCreacion": "2026-06-27T16:55:19.177456103",
      "fechaActualizacion": null,
      "cliente": {
        "id": 1,
        "nombre": "Carlos Mendoza",
        "correoElectronico": "carlos.mendoza@email.com",
        "fechaCreacion": null,
        "fechaActualizacion": null
      },
      "tecnicoAsignado": {
        "id": 1,
        "nombre": "Ana García",
        "especialidad": "Redes y Conectividad",
        "fechaCreacion": null,
        "fechaActualizacion": null
      }
    },
    "timestamp": "2026-06-27T16:56:38.034924465"
  }
  ```
* **Respuesta de Error (404 Not Found):**
  ```json
  {
    "success": false,
    "message": "No se encontró la solicitud con ID: 999",
    "timestamp": "2026-06-27T16:57:19.792501722"
  }
  ```

---

### 3. Registrar una nueva solicitud
Crea y persiste una nueva solicitud con sus relaciones de cliente y técnico en la base de datos.

* **Método:** `POST`
* **Ruta:** `/api/solicitudes`
* **Cuerpo de la Petición (Request Body):**
  ```json
  {
    "descripcion": "Problema con el correo corporativo",
    "estado": "ABIERTA",
    "cliente": {
      "nombre": "Luis Llatas",
      "correoElectronico": "luis@empresa.com"
    },
    "tecnicoAsignado": {
      "nombre": "Sofia Torres",
      "especialidad": "Software"
    }
  }
  ```
* **Respuesta Exitosa (201 Created):**
  ```json
  {
    "success": true,
    "message": "Solicitud creada exitosamente",
    "data": {
      "id": 4,
      "descripcion": "Problema con el correo corporativo",
      "estado": "ABIERTA",
      "fechaCreacion": "2026-06-27T16:56:38.633001057",
      "fechaActualizacion": null,
      "cliente": {
        "id": 4,
        "nombre": "Luis Llatas",
        "correoElectronico": "luis@empresa.com",
        "fechaCreacion": "2026-06-27T16:56:38.632900010",
        "fechaActualizacion": null
      },
      "tecnicoAsignado": {
        "id": 4,
        "nombre": "Sofia Torres",
        "especialidad": "Software",
        "fechaCreacion": "2026-06-27T16:56:38.632950020",
        "fechaActualizacion": null
      }
    },
    "timestamp": "2026-06-27T16:56:38.633060876"
  }
  ```
* **Respuesta de Error (400 Bad Request - Fallo de Validación):**
  ```json
  {
    "success": false,
    "message": "Los datos enviados contienen errores",
    "errors": {
      "cliente.nombre": "El nombre del cliente es obligatorio",
      "descripcion": "La descripción de la solicitud es obligatoria",
      "cliente.correoElectronico": "El correo electrónico debe tener un formato válido",
      "tecnicoAsignado.nombre": "El nombre del técnico es obligatorio",
      "tecnicoAsignado.especialidad": "La especialidad del técnico es obligatoria"
    },
    "timestamp": "2026-06-27T16:57:29.949016732"
  }
  ```

---

### 4. Actualizar una solicitud existente
Sobrescribe todos los campos de una solicitud existente buscando por su ID de ruta.

* **Método:** `PUT`
* **Ruta:** `/api/solicitudes/{id}`
* **Parámetros de Ruta:**
  * `id` (Long): Identificador de la solicitud a actualizar.
* **Cuerpo de la Petición (Request Body):**
  ```json
  {
    "descripcion": "Problema con el correo corporativo - URGENTE",
    "estado": "EN_PROCESO",
    "cliente": {
      "nombre": "Luis Llatas",
      "correoElectronico": "luis@empresa.com"
    },
    "tecnicoAsignado": {
      "nombre": "Sofia Torres",
      "especialidad": "Software"
    }
  }
  ```
* **Respuesta Exitosa (200 OK):**
  ```json
  {
    "success": true,
    "message": "Solicitud actualizada exitosamente",
    "data": {
      "id": 4,
      "descripcion": "Problema con el correo corporativo - URGENTE",
      "estado": "EN_PROCESO",
      "fechaCreacion": "2026-06-27T16:56:38.633001057",
      "fechaActualizacion": "2026-06-27T16:56:50.231061377",
      "cliente": {
        "id": 4,
        "nombre": "Luis Llatas",
        "correoElectronico": "luis@empresa.com",
        "fechaCreacion": "2026-06-27T16:56:38.632900010",
        "fechaActualizacion": "2026-06-27T16:56:50.230980110"
      },
      "tecnicoAsignado": {
        "id": 4,
        "nombre": "Sofia Torres",
        "especialidad": "Software",
        "fechaCreacion": "2026-06-27T16:56:38.632950020",
        "fechaActualizacion": "2026-06-27T16:56:50.231010040"
      }
    },
    "timestamp": "2026-06-27T16:56:50.231101828"
  }
  ```

---

### 5. Actualizar parcialmente el estado de una solicitud
Actualiza únicamente el estado (`ABIERTA`, `EN_PROCESO`, `CERRADA`) de una solicitud registrada.

* **Método:** `PATCH`
* **Ruta:** `/api/solicitudes/{id}/estado/{estado}`
* **Parámetros de Ruta:**
  * `id` (Long): Identificador de la solicitud.
  * `estado` (Enum): Nuevo estado (`ABIERTA`, `EN_PROCESO`, `CERRADA`).
* **Respuesta Exitosa (200 OK):**
  ```json
  {
    "success": true,
    "message": "Estado de la solicitud actualizado exitosamente",
    "data": {
      "id": 1,
      "descripcion": "No hay conexión a internet en el área de contabilidad",
      "estado": "EN_PROCESO",
      "fechaCreacion": "2026-06-27T16:55:19.177456103",
      "fechaActualizacion": "2026-06-27T17:01:10.450302190",
      "cliente": {
        "id": 1,
        "nombre": "Carlos Mendoza",
        "correoElectronico": "carlos.mendoza@email.com",
        "fechaCreacion": null,
        "fechaActualizacion": null
      },
      "tecnicoAsignado": {
        "id": 1,
        "nombre": "Ana García",
        "especialidad": "Redes y Conectividad",
        "fechaCreacion": null,
        "fechaActualizacion": null
      }
    },
    "timestamp": "2026-06-27T17:01:10.451000980"
  }
  ```

---

### 6. Eliminar una solicitud
Elimina físicamente el registro de la solicitud en la base de datos.

* **Método:** `DELETE`
* **Ruta:** `/api/solicitudes/{id}`
* **Parámetros de Ruta:**
  * `id` (Long): Identificador de la solicitud a eliminar.
* **Respuesta Exitosa (200 OK):**
  ```json
  {
    "success": true,
    "message": "Solicitud eliminada exitosamente",
    "data": null,
    "timestamp": "2026-06-27T16:58:05.105432190"
  }
  ```

---

## ⚠️ Manejo de Errores Globales

La API utiliza un controlador centralizado (`GlobalExceptionHandler`) para capturar las excepciones y estructurar los fallos del sistema bajo el mismo esquema de respuesta JSON de `ApiResponse` con `success: false`:

* **Errores de Validación (400 Bad Request):** Ocurre cuando un campo del request body marcado con anotaciones (`@NotBlank`, `@Email`, `@NotNull`) no cumple con las reglas. Los campos fallidos y sus mensajes correspondientes se detallan bajo la propiedad `"errors"`.
* **Recurso No Encontrado (404 Not Found):** Lanzado cuando se intenta buscar, actualizar o eliminar registros por IDs de Cliente, Técnico o Solicitud que no existen en MySQL.
* **Error de Servidor (500 Internal Server Error):** Captura cualquier fallo inesperado del servidor MySQL o código de negocio.
