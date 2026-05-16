# Casos de Prueba – SGISI
## Sistema de Gestión de Incidentes de Seguridad Informática – Banco Regional SA
### Etapa de pruebas – TP2 Módulo 2

---

## Criterios generales

- **Ambiente de prueba:** aplicación SGISI ejecutada localmente sobre JDK 21,
  conectada a la base de datos MySQL 8.0 con el schema `sgisi` inicializado
  mediante `sgisi_base_datos.sql`.
- **Datos de prueba:** los inserts del script SQL proveen los datos base
  (usuarios, activos, tipos de incidente, configuración SLA).
- **Resultado obtenido y estado:** se completan durante la ejecución del prototipo.
- **Trazabilidad:** cada caso referencia el requisito funcional que verifica.

---

## CP01 – Autenticación (UC01 / RF09)

### CP01-01 – Login con credenciales válidas

| Campo | Detalle |
|---|---|
| **Precondición** | El usuario `mlopez@bancoregio.com.ar` existe en la BD con estado activo = 1 |
| **Datos de entrada** | Email: `mlopez@bancoregio.com.ar` / Contraseña: `pass1234` |
| **Resultado esperado** | El sistema autentica al usuario y abre VentanaPrincipal con el menú correspondiente al rol ANALISTA_SOC |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP01-02 – Login con contraseña incorrecta

| Campo | Detalle |
|---|---|
| **Precondición** | El usuario existe en la BD |
| **Datos de entrada** | Email: `mlopez@bancoregio.com.ar` / Contraseña: `incorrecta` |
| **Resultado esperado** | El sistema muestra el mensaje "Credenciales inválidas" y no abre VentanaPrincipal |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP01-03 – Login con usuario inactivo

| Campo | Detalle |
|---|---|
| **Precondición** | El usuario `sromero@bancoregio.com.ar` tiene `activo = 0` (ejecutar: `UPDATE usuario SET activo = 0 WHERE id = 2`) |
| **Datos de entrada** | Email: `sromero@bancoregio.com.ar` / Contraseña: `pass5678` |
| **Resultado esperado** | El sistema muestra el mensaje "Usuario no encontrado" y no permite el acceso |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP01-04 – Login con email inexistente

| Campo | Detalle |
|---|---|
| **Precondición** | Ningún usuario con ese email en la BD |
| **Datos de entrada** | Email: `noexiste@bancoregio.com.ar` / Contraseña: `cualquiera` |
| **Resultado esperado** | El sistema muestra el mensaje "Usuario no encontrado" |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

---

## CP02 – Registro de incidente (UC02 / RF01)

### CP02-01 – Registro exitoso con todos los campos completos

| Campo | Detalle |
|---|---|
| **Precondición** | Usuario autenticado como Analista SOC |
| **Datos de entrada** | Tipo: MALWARE / Severidad: ALTO / Descripción: "Detección de keylogger en workstation WS-05" / Activo afectado: "Workstation WS-05" |
| **Resultado esperado** | El incidente queda registrado en la BD con estado DETECTADO. La fecha de vencimiento SLA se calcula como `fecha_deteccion + 8 horas`. Se registra una entrada en `bitacora_auditoria` con acción "REGISTRO". |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP02-02 – Cálculo automático de SLA según severidad CRITICO

| Campo | Detalle |
|---|---|
| **Precondición** | Usuario autenticado como Analista SOC |
| **Datos de entrada** | Tipo: RANSOMWARE / Severidad: CRITICO / resto de campos válidos |
| **Resultado esperado** | `fecha_vencimiento_sla` = `fecha_deteccion + 4 horas` (según `configuracion_sla` para severidad CRITICO) |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP02-03 – Intento de registro con campos obligatorios vacíos

| Campo | Detalle |
|---|---|
| **Precondición** | Usuario autenticado como Analista SOC |
| **Datos de entrada** | Tipo: seleccionado / Severidad: sin seleccionar / Descripción: vacía |
| **Resultado esperado** | El sistema resalta los campos faltantes y no realiza ninguna inserción en la BD |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

---

## CP03 – Cambio de estado (UC04 / RF03)

### CP03-01 – Transición válida: DETECTADO → EN_ANALISIS

| Campo | Detalle |
|---|---|
| **Precondición** | Existe el incidente con id=2 en estado DETECTADO (según datos de prueba del SQL). Usuario autenticado como Supervisor. |
| **Datos de entrada** | Incidente: id=2 / Nuevo estado: EN_ANALISIS / Observación: "Se asigna analista para investigación" |
| **Resultado esperado** | El estado del incidente se actualiza a EN_ANALISIS en la BD. Se registra entrada en `bitacora_auditoria` con acción "CAMBIO_ESTADO" y detalle "DETECTADO → EN_ANALISIS". |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP03-02 – Transición válida: EN_ANALISIS → CONTENIDO

| Campo | Detalle |
|---|---|
| **Precondición** | Existe un incidente en estado EN_ANALISIS. Usuario autenticado como Analista SOC. |
| **Datos de entrada** | Nuevo estado: CONTENIDO / Observación: "Amenaza aislada en servidor afectado" |
| **Resultado esperado** | Estado actualizado a CONTENIDO. Entrada registrada en bitácora. |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP03-03 – Transición inválida: DETECTADO → CERRADO

| Campo | Detalle |
|---|---|
| **Precondición** | Existe un incidente en estado DETECTADO |
| **Datos de entrada** | Nuevo estado: CERRADO |
| **Resultado esperado** | El sistema rechaza la operación y muestra un mensaje indicando que la transición no es válida desde el estado actual. La BD no se modifica. |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP03-04 – Transición inválida: CERRADO → EN_ANALISIS

| Campo | Detalle |
|---|---|
| **Precondición** | Existe el incidente con id=3 en estado CERRADO (según datos de prueba del SQL) |
| **Datos de entrada** | Nuevo estado: EN_ANALISIS |
| **Resultado esperado** | El sistema rechaza la operación. Un incidente cerrado no admite más transiciones. La BD no se modifica. |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP03-05 – Registro obligatorio en bitácora ante cada cambio

| Campo | Detalle |
|---|---|
| **Precondición** | Se ejecuta cualquier transición de estado válida |
| **Datos de entrada** | Cualquier transición válida con observación no vacía |
| **Resultado esperado** | Inmediatamente después del cambio, existe en `bitacora_auditoria` una nueva fila con el `incidente_id` correcto, el `usuario_id` del usuario autenticado, la acción "CAMBIO_ESTADO" y el detalle con los estados anterior y nuevo. Verificar con: `SELECT * FROM bitacora_auditoria ORDER BY id DESC LIMIT 1;` |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

---

## CP04 – Consulta de incidentes con filtros (UC08 / RF07)

### CP04-01 – Listar todos los incidentes abiertos

| Campo | Detalle |
|---|---|
| **Precondición** | Usuario autenticado. BD con al menos 2 incidentes en estado distinto de CERRADO y FALSO_POSITIVO. |
| **Datos de entrada** | Sin filtros aplicados |
| **Resultado esperado** | La tabla muestra los incidentes id=1 (EN_ANALISIS) e id=2 (DETECTADO). El incidente id=3 (CERRADO) no aparece. |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP04-02 – Filtro por severidad CRITICO

| Campo | Detalle |
|---|---|
| **Precondición** | Usuario autenticado. BD con datos de prueba cargados. |
| **Datos de entrada** | Filtro severidad: CRITICO |
| **Resultado esperado** | La tabla muestra únicamente el incidente id=1 (MALWARE / CRITICO). Los incidentes de otras severidades no aparecen. |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

### CP04-03 – Filtro sin resultados

| Campo | Detalle |
|---|---|
| **Precondición** | Usuario autenticado |
| **Datos de entrada** | Filtro tipo: FRAUDE_TRANSACCIONAL (no existe ninguno en los datos de prueba) |
| **Resultado esperado** | La tabla aparece vacía y el sistema muestra un mensaje informativo ("No se encontraron incidentes con los filtros seleccionados") |
| **Resultado obtenido** | |
| **Estado** | ☐ Pendiente ☐ Aprobado ☐ Fallido |

---

## Resumen de resultados

| ID | Caso de prueba | RF | Estado |
|---|---|---|---|
| CP01-01 | Login con credenciales válidas | RF09 | ☐ |
| CP01-02 | Login con contraseña incorrecta | RF09 | ☐ |
| CP01-03 | Login con usuario inactivo | RF09 | ☐ |
| CP01-04 | Login con email inexistente | RF09 | ☐ |
| CP02-01 | Registro de incidente completo | RF01 | ☐ |
| CP02-02 | Cálculo SLA severidad CRITICO | RF05 | ☐ |
| CP02-03 | Registro con campos vacíos | RF01 | ☐ |
| CP03-01 | Transición válida DETECTADO → EN_ANALISIS | RF03 | ☐ |
| CP03-02 | Transición válida EN_ANALISIS → CONTENIDO | RF03 | ☐ |
| CP03-03 | Transición inválida DETECTADO → CERRADO | RF03 | ☐ |
| CP03-04 | Transición inválida CERRADO → EN_ANALISIS | RF03 | ☐ |
| CP03-05 | Registro en bitácora ante cambio de estado | RF10 | ☐ |
| CP04-01 | Listar incidentes abiertos sin filtros | RF07 | ☐ |
| CP04-02 | Filtro por severidad CRITICO | RF07 | ☐ |
| CP04-03 | Filtro sin resultados | RF07 | ☐ |

**Total casos: 15 | Aprobados: — | Fallidos: — | Pendientes: 15**
