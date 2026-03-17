# LogiTrack - Sistema de Gestion de Bodegas

Proyecto final desarrollado en Spring Boot para la empresa LogiTrack S.A.
El sistema permite gestionar bodegas, productos, clientes y movimientos de inventario.

---

## ¿Que hace el proyecto?

LogiTrack es una empresa que administra bodegas en distintas ciudades de colombia.
Antes manejaban todo en excel, este sistema reemplaza eso con una API REST completa.

Funcionalidades principales:
- CRUD de bodegas, productos, clientes, categorias y usuarios
- Registro de movimientos de inventario (entradas, salidas y transferencias)
- Autenticacion con JWT
- Documentacion con Swagger
- Frontend en HTML/CSS/JS

---

## Tecnologias

- Java 21
- Spring Boot 3.5.11
- MySQL 8
- Spring Security + JWT
- Swagger (springdoc)
- Lombok
- HTML + CSS + JS 

---

## Como ejecutarlo

### Requisitos
- Tener Java 21 instalado
- MySQL corriendo en el equipo
- IntelliJ IDEA

### Pasos

1. Clonar el repositorio
2. Ejecutar el `schema.sql` en MySQL Workbench para crear las tablas
3. Ejecutar el `data.sql` para insertar datos de prueba
4. Configurar la contraseña de MySQL en `application.properties`:

```properties
spring.datasource.password=ejemplo1234
```

5. Correr el proyecto desde IntelliJ con el boton Run

El servidor queda en `http://localhost:8080`

---

## Credenciales de prueba

| Email                   | Contraseña | Rol |
|-------------------------|------------|---|
| juan@logitrack.com      | juan123    | ADMIN |
| jmendez10@logitrack.com | 1234       | EMPLEADO |

---

## Swagger

Para ver y probar todos los endpoints:
```
http://localhost:8080/swagger-ui/index.html
```

---

## Estructura del proyecto

```
src/
├── auth/
├── config/
├── controller/
├── DTO/
├── exception/
├── mapper/
├── model/
├── repository/
└── service/
```

---

## Notas

- El frontend esta en `src/main/resources/static/`
- Para acceder desde otro dispositivo en la misma red hay que agregar la IP en el SecurityConfig y abrir el puerto 8080 en el firewall de Windows
- Los scripts SQL estan en la raiz del proyecto

---

Desarrollado por Juan Pablo Bareño Sierra - Marzo 2026