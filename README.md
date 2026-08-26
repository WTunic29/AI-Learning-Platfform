# AI Learning Platform

Aplicación móvil educativa desarrollada para la plataforma **AI Learning**, cuyo objetivo es proporcionar una base tecnológica para futuras funcionalidades de aprendizaje.

Este repositorio contiene el desarrollo correspondiente al **Sprint 1**, enfocado principalmente en la implementación de usuarios, registro y autenticación.

---

## Estado actual del proyecto

### Sprint 1 — Autenticación y gestión básica de usuarios

Durante este Sprint se implementaron y probaron las siguientes historias de usuario:

- **HU-01 — Registro de usuario**
- **HU-02 — Inicio de sesión**

El sistema permite actualmente:

- Registrar usuarios.
- Persistir usuarios en PostgreSQL.
- Validar correos existentes.
- Almacenar contraseñas utilizando BCrypt.
- Iniciar sesión mediante correo y contraseña.
- Validar las credenciales desde el backend.
- Consumir la API REST desde Android.
- Mostrar el resultado del login en la aplicación.
- Manejar errores de conexión desde Android.
- Probar los endpoints mediante Postman.

---

# Arquitectura del sistema

El proyecto utiliza una arquitectura **cliente-servidor**, donde la aplicación Android funciona como cliente y el backend desarrollado con Spring Boot proporciona una API REST.

```text
┌──────────────────────────────────────┐
│            ANDROID APP               │
│                                      │
│          Java / Android              │
│                                      │
│       MainActivity                   │
│            │                         │
│            ▼                         │
│      Retrofit / OkHttp               │
└──────────────┬───────────────────────┘
               │
               │ HTTP / JSON
               ▼
┌──────────────────────────────────────┐
│             BACKEND                  │
│                                      │
│           Spring Boot                │
│                                      │
│          Controller                  │
│              │                       │
│              ▼                       │
│            Service                   │
│              │                       │
│              ▼                       │
│          Repository                 │
└──────────────┬───────────────────────┘
               │
               │ JPA / Hibernate
               ▼
┌──────────────────────────────────────┐
│            PostgreSQL                │
│                                      │
│              Usuario                 │
└──────────────────────────────────────┘
```

## Arquitectura Backend

El backend utiliza una arquitectura por capas:

```text
Controller
     │
     ▼
Service
     │
     ▼
Repository
     │
     ▼
PostgreSQL
```

### Controller
Recibe las solicitudes HTTP provenientes de Android o Postman.

Endpoints principales:

```text
POST /api/auth/register
POST /api/auth/login
```

### Service
Contiene la lógica de negocio relacionada con usuarios y autenticación:

- Registrar usuarios.
- Buscar usuarios por correo.
- Validar credenciales.
- Comparar contraseñas mediante BCrypt.
- Generar respuestas.

### Repository
Utiliza Spring Data JPA para abstraer el acceso y la persistencia de datos.

### PostgreSQL
Base de datos relacional utilizada para almacenar la información de usuarios. Las contraseñas se almacenan mediante hash BCrypt y no como texto plano.

---

# Arquitectura Android

La aplicación móvil utiliza Java, Android Studio, Android SDK, Retrofit y OkHttp.

Flujo principal:

```text
MainActivity
      │
      ▼
LoginRequest
      │
      ▼
ApiService
      │
      ▼
RetrofitClient
      │
      ▼
Spring Boot API
      │
      ▼
LoginResponse
      │
      ▼
MainActivity
```

## Estructura principal

```text
AI-Learning-Mobile/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── co/
│           │       └── edu/
│           │           └── unipiloto/
│           │               └── ailearningmobile/
│           │                   ├── dto/
│           │                   │   ├── LoginRequest.java
│           │                   │   └── LoginResponse.java
│           │                   ├── network/
│           │                   │   ├── ApiService.java
│           │                   │   └── RetrofitClient.java
│           │                   └── MainActivity.java
│           ├── res/
│           │   └── layout/
│           │       └── activity_main.xml
│           └── AndroidManifest.xml
│
└── build.gradle.kts
```

---

# HU-01 — Registro de usuario

## Descripción

Como usuario, quiero crear una cuenta para poder acceder a la plataforma.

## Flujo

```text
Android / Postman
        │
        ▼
POST /api/auth/register
        │
        ▼
AuthController
        │
        ▼
AuthService
        │
        ├── Validar información
        ├── Comprobar correo
        └── Generar hash BCrypt
                │
                ▼
        UsuarioRepository
                │
                ▼
           PostgreSQL
```

### Ejemplo de solicitud

```http
POST /api/auth/register
Content-Type: application/json
```

```json
{
    "nombre": "Christian",
    "correo": "christian@example.com",
    "password": "Password123"
}
```

---

# HU-02 — Inicio de sesión

## Descripción

Como usuario registrado, quiero iniciar sesión con mi correo y contraseña para acceder a la plataforma.

## Endpoint

```http
POST /api/auth/login
```

## Request

```json
{
    "correo": "christian@example.com",
    "password": "Password123"
}
```

## Flujo

```text
Correo
  │
  ▼
Buscar usuario
  │
  ▼
Usuario encontrado
  │
  ▼
Comparar contraseña con BCrypt
  │
  ▼
¿Credenciales correctas?
       │
   ┌───┴───┐
   │       │
  Sí       No
   │       │
   ▼       ▼
 Login    Error
exitoso
```

---

# BCrypt

El sistema utiliza BCrypt para proteger las contraseñas almacenadas.

```text
Contraseña introducida
        │
        ▼
      BCrypt
        │
        ▼
Comparación con hash almacenado
        │
        ▼
   ¿Coincide?
    /      \
  Sí        No
  │          │
  ▼          ▼
Login      Error
```

---

# Comunicación REST

La aplicación Android consume el backend mediante una API REST y JSON.

```text
Android
   │
   │ POST /api/auth/login
   ▼
Spring Boot
   │
   ▼
AuthController
   │
   ▼
AuthService
   │
   ▼
PostgreSQL
```

---

# Retrofit y OkHttp

Retrofit permite definir los endpoints mediante interfaces:

```java
@POST("api/auth/login")
Call<LoginResponse> login(@Body LoginRequest request);
```

Flujo:

```text
MainActivity
      │
      ▼
ApiService
      │
      ▼
Retrofit
      │
      ▼
OkHttp
      │
      ▼
HTTP Request
      │
      ▼
Spring Boot
```

---

# Configuración de comunicación local

Durante las pruebas con el emulador Android se utilizó:

```bash
adb reverse tcp:8080 tcp:8080
```

La aplicación utiliza:

```text
http://127.0.0.1:8080/
```

cuando el redireccionamiento mediante `adb reverse` está activo.

---

# Pruebas realizadas

Las funcionalidades del Sprint fueron probadas utilizando:

- Postman.
- Android Studio.
- Emulador Pixel 8.
- Logcat.

## Caso 1 — Registro exitoso

```text
POST /api/auth/register
        │
        ▼
Spring Boot
        │
        ▼
PostgreSQL
```

**Resultado:** Exitoso.

## Caso 2 — Registro con correo existente

Se intenta registrar nuevamente un usuario con un correo que ya existe.

**Resultado:** Validado.

## Caso 3 — Login correcto

Credenciales:

```text
Correo: christian@example.com
Contraseña: Password123
```

Respuesta:

```text
HTTP 200 OK
```

Logcat:

```text
Login exitoso: Christian
```

**Resultado:** Exitoso.

## Caso 4 — Contraseña incorrecta

Se utiliza un correo válido junto con una contraseña incorrecta.

**Resultado:** Validado.

## Caso 5 — Servidor apagado

Cuando el backend no está disponible, Android detecta el fallo mediante el callback de Retrofit.

**Resultado:** Validado.

---

# Herramientas utilizadas

| Herramienta | Función |
|---|---|
| IntelliJ IDEA | Desarrollo backend |
| Android Studio | Desarrollo Android |
| Postman | Pruebas API |
| PostgreSQL | Base de datos |
| Git | Control de versiones |
| GitHub | Repositorio |

---

# Tecnologías

## Android

| Tecnología | Función |
|---|---|
| Java | Lenguaje de programación |
| Android Studio | IDE |
| Android SDK | Desarrollo móvil |
| Retrofit | Cliente REST |
| OkHttp | Cliente HTTP |
| JSON | Intercambio de datos |

## Backend

| Tecnología | Función |
|---|---|
| Java | Lenguaje de programación |
| Spring Boot | Framework backend |
| Spring Web | API REST |
| Spring Data JPA | Persistencia |
| Hibernate | ORM |
| BCrypt | Protección de contraseñas |

## Base de datos

| Tecnología | Función |
|---|---|
| PostgreSQL | Base de datos relacional |

---

# Base de datos

La persistencia utiliza:

```text
Spring Data JPA
        │
        ▼
Hibernate
        │
        ▼
PostgreSQL
```

La entidad principal implementada durante este Sprint es:

```text
Usuario
```

---

# Configuración y seguridad

Las credenciales reales de la base de datos no deben almacenarse en el repositorio público.

No publicar valores reales de:

```properties
spring.datasource.password
```

Se recomienda utilizar:

```text
application-example.properties
```

Ejemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ai_learning
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
```

El archivo con credenciales reales debe estar incluido en `.gitignore`.

---

# Ejecución del Backend

## Requisitos

- JDK 21.
- IntelliJ IDEA.
- PostgreSQL.
- Maven/Gradle según la configuración del proyecto.

## Pasos

1. Abrir el proyecto backend en IntelliJ IDEA.
2. Verificar que PostgreSQL esté ejecutándose.
3. Configurar las credenciales de PostgreSQL.
4. Ejecutar `AiLearningApiApplication`.
5. Verificar que Spring Boot inicie correctamente.

Puerto:

```text
8080
```

URL local:

```text
http://localhost:8080
```

---

# Ejecución de Android

## Requisitos

- Android Studio.
- Android SDK.
- Emulador Android.
- JDK compatible con el proyecto.

## Pasos

1. Abrir el proyecto `AILearningMobile`.
2. Iniciar el emulador Pixel 8.
3. Verificar que el backend esté ejecutándose.
4. Configurar:

```bash
adb reverse tcp:8080 tcp:8080
```

5. Ejecutar la aplicación.
6. Introducir las credenciales.
7. Presionar **Ingresar**.

---

# Alcance del Sprint

## Incluido

- Registro.
- Login.
- Persistencia.
- PostgreSQL.
- BCrypt.
- API REST.
- Integración Android-Backend.
- Pruebas Postman.
- Pruebas Android.
- Manejo de errores de conexión.

## No incluido todavía

- JWT.
- Refresh Token.
- Recuperación de contraseña.
- Gestión avanzada de sesiones.
- Perfil completo del usuario.
- Funcionalidades avanzadas de aprendizaje.
- Sistema completo de cursos.
- Sistema de progreso.
- Funcionalidades de inteligencia artificial.

Estas funcionalidades podrán incorporarse en futuros Sprints.

---

# Próximos Sprints

```text
Sprint 1
│
├── Registro
└── Login
        │
        ▼
Sprint 2
│
├── JWT
├── Sesiones
└── Perfil
        │
        ▼
Sprint 3
│
├── Contenido educativo
├── Cursos
└── Progreso
        │
        ▼
Sprints posteriores
│
├── Funcionalidades de IA
├── Recomendaciones
└── Analítica de aprendizaje
```

---

# Definition of Done

## Infraestructura

-  Android Studio configurado.
-  IntelliJ IDEA configurado.
-  JDK 21 configurado.
-  PostgreSQL configurado.
-  Git configurado.
-  Postman configurado.

## Backend

-  Spring Boot funcionando.
-  PostgreSQL conectado.
-  Spring Data JPA configurado.
-  Entidad `Usuario`.
-  `UsuarioRepository`.
-  DTOs.
-  `AuthService`.
-  `AuthController`.
-  BCrypt.

## HU-01

-  Endpoint de registro.
-  Registro de usuarios.
-  Validación de correo.
-  Correo único.
-  Hash de contraseña.
-  Persistencia en PostgreSQL.
-  Prueba mediante Postman.
-  Prueba desde Android.

## HU-02

-  Endpoint de login.
-  Búsqueda de usuario.
-  Validación de contraseña.
-  BCrypt.
-  Respuesta de autenticación.
-  Prueba mediante Postman.
-  Prueba desde Android.
-  Manejo de errores.
-  Comunicación Android-Backend.

## Entrega

-  Pruebas Postman.
-  Pruebas Android.
-  Evidencias.
-  Código funcional.
-  README.
-  Commit final.
-  Push final a GitHub.

---

#  Control de versiones

Antes de realizar la entrega:

```bash
git status
```

Después:

```bash
git add .
```

Commit:

```bash
git commit -m "feat: implement user registration and login"
```

Push:

```bash
git push
```

Antes del `push`, verificar que no se incluyan `application.properties` ni otros archivos con credenciales reales.

---

# Resultado final del Sprint 1

El Sprint establece una primera versión funcional de **AI Learning Platform**:

```text
                    PostgreSQL
                         ▲
                         │
                    JPA / Hibernate
                         ▲
                         │
                    Spring Boot
                         ▲
                         │
                     REST API
                         ▲
                         │
                  Retrofit / OkHttp
                         ▲
                         │
                    Android App
```

El usuario puede:

```text
Crear una cuenta
      │
      ▼
Usuario almacenado
en PostgreSQL
      │
      ▼
Introducir credenciales
      │
      ▼
Backend valida BCrypt
      │
      ▼
Credenciales correctas
      │
      ▼
Login exitoso
```

Con esto se establece la base técnica necesaria para continuar con los siguientes Sprints.

---

# Proyecto

**AI Learning Platform**

### Sprint

**Sprint 1 — Autenticación y gestión básica de usuarios**

### Historias implementadas

- **HU-01 — Registro de usuario**
- **HU-02 — Inicio de sesión**

### Stack principal

```text
Android
Java
Retrofit
OkHttp
        │
        ▼
Spring Boot
Spring Web
Spring Data JPA
Hibernate
BCrypt
        │
        ▼
PostgreSQL
```

---

## Nota

Este README documenta el alcance y la arquitectura correspondiente al **primer Sprint**. Las funcionalidades adicionales serán incorporadas progresivamente en los siguientes incrementos del proyecto.
