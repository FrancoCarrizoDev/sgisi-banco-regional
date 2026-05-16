# SGISI Prototipo Java 21 + Swing + JDBC

## Requisitos
- Java 21
- Maven 3.9+
- Docker + Docker Compose

## Variables de entorno
- `SGISI_DB_URL`
- `SGISI_DB_USER`
- `SGISI_DB_PASSWORD`

Si no existen, se usa `src/main/resources/application.properties`.

## Usuarios demo
- `mlopez@bancoregio.com.ar` / `pass1234`
- `sromero@bancoregio.com.ar` / `pass5678`
- `agimenez@bancoregio.com.ar` / `sup@2024`
- `cferreyra@bancoregio.com.ar` / `adm@2024`

## Docker MySQL (manual)
1. `docker compose up -d`
2. El init usa `src/src/main/resources/docker-init.sql` con estrategia NO destructiva.

## Maven (manual)
1. `mvn clean compile`
2. `mvn exec:java`

## Casos de uso incluidos
- UC01 Login
- UC02 Registrar incidente
- UC04 Cambiar estado
- UC08 Listar incidentes con filtros

## Validación manual sugerida
1. Login válido e inválido.
2. Registrar incidente: verificar creación + bitácora `REGISTRO`.
3. Forzar falla de bitácora y validar rollback de UC02.
4. Cambiar estado válido e inválido; verificar `CAMBIO_ESTADO`.
5. Filtrar por estado/severidad en JTable.

## Alcance excluido
Sin CRUD de usuarios, reportes, evidencia, notificaciones ni UI de consultas de auditoría.
