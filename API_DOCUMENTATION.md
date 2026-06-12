# 🛠️ API de Gestión de Solicitudes de Soporte Técnico

Esta es la documentación oficial de la API REST para el sistema de solicitudes de soporte técnico. La API permite registrar, consultar, actualizar y eliminar solicitudes, asignando clientes y técnicos responsables.

---

## 🚀 Información General

* **Tecnologías:**
  * Java 25
  * Spring Boot 4.0.6
  * Springdoc OpenAPI 2.8.8 (Swagger)
  * Persistencia: Capa de Repositorio (`ISolicitudRepository` y `SolicitudRepositoryImpl`) con base de datos simulada en memoria (`LinkedHashMap` thread-safe).
* **Servidor Local:** `http://localhost:8080`
* **Documentación Interactiva:**
  * **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  * **OpenAPI Specs (JSON):** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 📦 Modelos de Datos

### 1. Solicitud
Representa una solicitud de soporte técnico en el sistema.

| Campo | Tipo | Validaciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | Long | - | Identificador único autogenerado. |
| `descripcion` | String | `@NotBlank` | Descripción detallada del problema. |
| `estado` | Enum | `@NotNull` | Estado actual de la solicitud (`ABIERTA`, `EN_PROCESO`, `CERRADA`). |
| `fechaCreacion` | LocalDateTime | - | Fecha y hora en la que se creó la solicitud (autogenerada). |
| `fechaActualizacion` | LocalDateTime | - | Fecha y hora de la última modificación (nulo inicialmente). |
| `cliente` | Cliente | `@NotNull`, `@Valid` | Información del cliente que solicita el soporte. |
| `tecnicoAsignado` | Tecnico | `@NotNull`, `@Valid` | Información del técnico asignado a la solicitud. |

### 2. Cliente
Datos del cliente que reporta el problema.

| Campo | Tipo | Validaciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | Long | - | Identificador único del cliente (autogenerado si se envía vacío). |
| `nombre` | String | `@NotBlank` | Nombre completo del cliente. |
| `correoElectronico` | String | `@NotBlank`, `@Email` | Correo electrónico de contacto del cliente. |

### 3. Técnico
Datos del especialista que resolverá el problema.

| Campo | Tipo | Validaciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | Long | - | Identificador único del técnico (autogenerado si se envía vacío). |
| `nombre` | String | `@NotBlank` | Nombre completo del técnico. |
| `especialidad` | String | `@NotBlank` | Especialidad técnica (ej. "Hardware", "Redes", etc.). |

### 4. ApiResponse (Envoltura Genérica)
Estructura unificada de comunicación para todas las respuestas de la API.

| Campo | Tipo | Descripción |
| :--- | :--- | :--- |
| `success` | boolean | `true` si la operación se completó con éxito, `false` en caso de error o validación fallida. |
| `message` | String | Mensaje explicativo para el usuario o desarrollador sobre el resultado. |
| `data` | T (Genérico) | Datos de respuesta de negocio (será `null` en caso de error). |
| `errors` | Object | Diccionario de errores detallados (usado en validaciones de campos fallidos). |
| `timestamp` | LocalDateTime | Fecha y hora en la que se generó la respuesta en el servidor. |

---

## 🛤️ Endpoints del API (`/api/solicitudes`)

### 1. Obtener todas las solicitudes
Retorna una lista completa de todas las solicitudes registradas en memoria, opcionalmente filtradas por estado.

* **Método:** `GET`
* **Ruta:** `/api/solicitudes`
* **Parámetros de Consulta (Query Params - Opcional):**
  * `estado` (Enum): Filtrar por el estado de la solicitud (`ABIERTA`, `EN_PROCESO`, `CERRADA`). Ej: `/api/solicitudes?estado=ABIERTA`
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
        "fechaCreacion": "2026-06-08T19:45:00",
        "cliente": {
          "id": 1,
          "nombre": "Carlos Mendoza",
          "correoElectronico": "carlos.mendoza@email.com"
        },
        "tecnicoAsignado": {
          "id": 1,
          "nombre": "Ana García",
          "especialidad": "Redes y Conectividad"
        }
      },
      {
        "id": 2,
        "descripcion": "La impresora del piso 3 no imprime correctamente",
        "estado": "EN_PROCESO",
        "fechaCreacion": "2026-06-06T19:45:00",
        "cliente": {
          "id": 2,
          "nombre": "María López",
          "correoElectronico": "maria.lopez@email.com"
        },
        "tecnicoAsignado": {
          "id": 2,
          "nombre": "Pedro Ruiz",
          "especialidad": "Hardware"
        }
      }
    ],
    "timestamp": "2026-06-12T14:11:47"
  }
  ```

---

### 2. Buscar solicitud por ID
Busca una única solicitud a partir de su ID y la retorna envuelta.

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
      "fechaCreacion": "2026-06-08T19:45:00",
      "cliente": {
        "id": 1,
        "nombre": "Carlos Mendoza",
        "correoElectronico": "carlos.mendoza@email.com"
      },
      "tecnicoAsignado": {
        "id": 1,
        "nombre": "Ana García",
        "especialidad": "Redes y Conectividad"
      }
    },
    "timestamp": "2026-06-12T14:12:00"
  }
  ```
* **Respuesta de Error (404 Not Found):**
  ```json
  {
    "success": false,
    "message": "No se encontró la solicitud con ID: 99",
    "timestamp": "2026-06-12T14:12:05"
  }
  ```

---

### 3. Registrar una nueva solicitud
Registra una solicitud en el sistema. Le asigna un ID, fecha de creación y la devuelve envuelta en el formato de éxito.

* **Método:** `POST`
* **Ruta:** `/api/solicitudes`
* **Cuerpo de la Petición (Request Body):**
  ```json
  {
    "descripcion": "Fallo en el disco duro del servidor de desarrollo",
    "estado": "ABIERTA",
    "cliente": {
      "nombre": "Roberto Santisteban",
      "correoElectronico": "roberto.s@email.com"
    },
    "tecnicoAsignado": {
      "nombre": "Pedro Ruiz",
      "especialidad": "Hardware"
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
      "descripcion": "Fallo en el disco duro del servidor de desarrollo",
      "estado": "ABIERTA",
      "fechaCreacion": "2026-06-08T19:48:32.456",
      "cliente": {
        "id": 401,
        "nombre": "Roberto Santisteban",
        "correoElectronico": "roberto.s@email.com"
      },
      "tecnicoAsignado": {
        "id": 402,
        "nombre": "Pedro Ruiz",
        "especialidad": "Hardware"
      }
    },
    "timestamp": "2026-06-12T14:12:15"
  }
  ```
* **Respuesta de Error (400 Bad Request - Errores de Validación):**
  ```json
  {
    "success": false,
    "message": "Los datos enviados contienen errores",
    "errors": {
      "descripcion": "La descripción de la solicitud es obligatoria",
      "cliente.correoElectronico": "El correo electrónico debe tener un formato válido"
    },
    "timestamp": "2026-06-12T14:12:20"
  }
  ```

---

### 4. Actualizar una solicitud existente
Sobrescribe todos los campos de una solicitud existente según su ID. La respuesta exitosa se devuelve envuelta.

* **Método:** `PUT`
* **Ruta:** `/api/solicitudes/{id}`
* **Parámetros de Ruta:**
  * `id` (Long): Identificador de la solicitud a actualizar.
* **Cuerpo de la Petición (Request Body):**
  ```json
  {
    "descripcion": "No hay conexión a internet en el área de contabilidad - Resuelto provisionalmente con backup de 4G",
    "estado": "EN_PROCESO",
    "cliente": {
      "id": 1,
      "nombre": "Carlos Mendoza",
      "correoElectronico": "carlos.mendoza@email.com"
    },
    "tecnicoAsignado": {
      "id": 1,
      "nombre": "Ana García",
      "especialidad": "Redes y Conectividad"
    }
  }
  ```
* **Respuesta Exitosa (200 OK):**
  ```json
  {
    "success": true,
    "message": "Solicitud actualizada exitosamente",
    "data": {
      "id": 1,
      "descripcion": "No hay conexión a internet en el área de contabilidad - Resuelto provisionalmente con backup de 4G",
      "estado": "EN_PROCESO",
      "fechaCreacion": "2026-06-08T19:45:00",
      "cliente": {
        "id": 1,
        "nombre": "Carlos Mendoza",
        "correoElectronico": "carlos.mendoza@email.com"
      },
      "tecnicoAsignado": {
        "id": 1,
        "nombre": "Ana García",
        "especialidad": "Redes y Conectividad"
      }
    },
    "timestamp": "2026-06-12T14:12:35"
  }
  ```
* **Respuesta de Error (404 Not Found):**
  ```json
  {
    "success": false,
    "message": "No se encontró la solicitud con ID: 99",
    "timestamp": "2026-06-12T14:12:40"
  }
  ```

---

### 5. Actualizar el estado de una solicitud
Actualiza únicamente el estado (`ABIERTA`, `EN_PROCESO`, `CERRADA`) de una solicitud a partir de su ID y el estado especificado en la ruta.

* **Método:** `PUT`
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
      "fechaCreacion": "2026-06-08T19:45:00",
      "fechaActualizacion": "2026-06-12T16:35:10",
      "cliente": {
        "id": 1,
        "nombre": "Carlos Mendoza",
        "correoElectronico": "carlos.mendoza@email.com"
      },
      "tecnicoAsignado": {
        "id": 1,
        "nombre": "Ana García",
        "especialidad": "Redes y Conectividad"
      }
    },
    "timestamp": "2026-06-12T16:35:10"
  }
  ```
* **Respuesta de Error (404 Not Found):**
  ```json
  {
    "success": false,
    "message": "No se encontró la solicitud con ID: 99",
    "timestamp": "2026-06-12T16:35:15"
  }
  ```

---

### 6. Eliminar una solicitud
Elimina del sistema una solicitud a partir de su ID. Devuelve una respuesta de éxito con `data` nulo en formato JSON.

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
    "timestamp": "2026-06-12T14:13:00"
  }
  ```
* **Respuesta de Error (404 Not Found):**
  ```json
  {
    "success": false,
    "message": "No se encontró la solicitud con ID: 99",
    "timestamp": "2026-06-12T14:13:05"
  }
  ```

---

## ⚠️ Manejo de Errores Globales

La API utiliza un controlador centralizado (`GlobalExceptionHandler`) para atrapar excepciones y transformarlas en una estructura estándar `ApiResponse` con la propiedad `success: false`:

```json
{
  "success": false,
  "message": "Mensaje comprensible del error",
  "errors": {
    "campo_invalido": "Descripción detallada del fallo"
  },
  "timestamp": "Fecha y hora del error"
}
```

* **Errores de Validación (400 Bad Request):** Ocurre cuando fallan restricciones como `@NotBlank`, `@Email` o `@NotNull` en peticiones `POST` o `PUT`. Los detalles se inyectan en `errors`.
* **Recurso No Encontrado (404 Not Found):** Ocurre cuando se busca, edita o elimina una solicitud con un ID inexistente. El campo `errors` permanece ausente/nulo.
* **Errores Internos (500 Internal Server Error):** Captura cualquier excepción imprevista en el sistema.
