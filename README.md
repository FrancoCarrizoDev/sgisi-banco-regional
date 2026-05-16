# SGISI - Sistema de Gestion de Incidentes de Seguridad Informatica

Proyecto academico para la materia Seminario de Practica Informatica.

## Contexto

- Alumno: Carrizo, Franco Adrian
- Organizacion: Banco Regional SA
- Tecnologia: Java 21, Swing, JDBC, MySQL 8.0, Maven y Docker Compose
- Metodologia: Proceso Unificado de Desarrollo (PUD)

## Estructura

- `src/`: prototipo operacional Java.
- `base_datos/`: script MySQL con DDL, DML, consultas y ejemplos de borrado.
- `diagramas/`: diagramas UML, entidad-relacion y red en PlantUML e imagenes.
- `documentos/`: documentos complementarios de comunicacion, pruebas y entregas de trabajo.
- `docker-compose.yml`: entorno local de base de datos MySQL para el prototipo.

## Ejecucion del prototipo

Requisitos:

- Java 21
- Maven 3.9+
- Docker y Docker Compose

Pasos:

```bash
docker compose up -d
cd src
mvn clean compile
mvn exec:java
```

## Usuarios demo

- `mlopez@bancoregio.com.ar` / `pass1234`
- `sromero@bancoregio.com.ar` / `pass5678`
- `agimenez@bancoregio.com.ar` / `sup@2024`
- `cferreyra@bancoregio.com.ar` / `adm@2024`

## Alcance del prototipo

Casos de uso implementados:

- UC01: iniciar sesion.
- UC02: registrar incidente.
- UC04: cambiar estado del incidente.
- UC08: listar incidentes con filtros.

Quedan fuera del prototipo: CRUD de usuarios, reportes avanzados, gestion de evidencias, notificaciones y auditoria visual completa.
