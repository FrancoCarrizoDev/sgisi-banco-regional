-- ============================================================
-- SGISI – Sistema de Gestión de Incidentes de Seguridad Informática
-- Banco Regional SA
-- Base de datos: MySQL 8.0
-- Archivo: sgisi_base_datos.sql
-- Versión: 1.0
-- ============================================================

-- ============================================================
-- SECCIÓN 1: DDL – DEFINICIÓN DE ESTRUCTURA
-- ============================================================

CREATE DATABASE IF NOT EXISTS sgisi
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sgisi;

-- ------------------------------------------------------------
-- 1.1 tipo_incidente
--     Catálogo de clasificaciones de incidentes de seguridad.
--     Corresponde al enum TipoIncidente del diagrama de dominio.
-- ------------------------------------------------------------
CREATE TABLE tipo_incidente (
    id          TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(50)  NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    CONSTRAINT pk_tipo_incidente PRIMARY KEY (id),
    CONSTRAINT uq_tipo_nombre    UNIQUE (nombre)
) ENGINE = InnoDB
  COMMENT = 'Catálogo de tipos de incidente de seguridad';

-- ------------------------------------------------------------
-- 1.2 severidad
--     Niveles de severidad (P1–P4). nivel_prioridad permite
--     ordenar de mayor a menor urgencia (1 = Crítico).
-- ------------------------------------------------------------
CREATE TABLE severidad (
    id              TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(20) NOT NULL,
    nivel_prioridad TINYINT    NOT NULL,
    CONSTRAINT pk_severidad        PRIMARY KEY (id),
    CONSTRAINT uq_severidad_nombre UNIQUE (nombre),
    CONSTRAINT uq_severidad_nivel  UNIQUE (nivel_prioridad),
    CONSTRAINT ck_nivel CHECK (nivel_prioridad BETWEEN 1 AND 4)
) ENGINE = InnoDB
  COMMENT = 'Niveles de severidad: CRITICO, ALTO, MEDIO, BAJO';

-- ------------------------------------------------------------
-- 1.3 estado_incidente
--     Estados válidos del ciclo de vida del incidente.
-- ------------------------------------------------------------
CREATE TABLE estado_incidente (
    id     TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    CONSTRAINT pk_estado       PRIMARY KEY (id),
    CONSTRAINT uq_estado_nombre UNIQUE (nombre)
) ENGINE = InnoDB
  COMMENT = 'Estados del ciclo de vida del incidente';

-- ------------------------------------------------------------
-- 1.4 configuracion_sla
--     Define el plazo máximo de resolución por severidad.
--     Relación 1:1 con severidad (una configuración por nivel).
-- ------------------------------------------------------------
CREATE TABLE configuracion_sla (
    id           TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    severidad_id TINYINT UNSIGNED NOT NULL,
    plazo_horas  SMALLINT UNSIGNED NOT NULL,
    descripcion  VARCHAR(100),
    CONSTRAINT pk_sla           PRIMARY KEY (id),
    CONSTRAINT fk_sla_severidad FOREIGN KEY (severidad_id)
        REFERENCES severidad (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT uq_sla_severidad UNIQUE (severidad_id)
) ENGINE = InnoDB
  COMMENT = 'Plazos SLA por nivel de severidad';

-- ------------------------------------------------------------
-- 1.5 usuario
--     Tabla única para todos los roles (Single Table Inheritance).
--     El discriminador 'rol' distingue AnalistaSoc,
--     SupervisorSeguridad y Administrador.
-- ------------------------------------------------------------
CREATE TABLE usuario (
    id              INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(100) NOT NULL,
    apellido        VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL,
    legajo          VARCHAR(20),
    contrasena_hash VARCHAR(255) NOT NULL,
    rol             ENUM('ANALISTA_SOC', 'SUPERVISOR_SEGURIDAD', 'ADMINISTRADOR') NOT NULL,
    activo          TINYINT(1)   NOT NULL DEFAULT 1,
    fecha_creacion  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_usuario  PRIMARY KEY (id),
    CONSTRAINT uq_email    UNIQUE (email),
    CONSTRAINT uq_legajo   UNIQUE (legajo)
) ENGINE = InnoDB
  COMMENT = 'Usuarios del sistema con discriminador de rol';

-- ------------------------------------------------------------
-- 1.6 activo_afectado
--     Activos de información del banco que pueden verse
--     comprometidos en un incidente.
-- ------------------------------------------------------------
CREATE TABLE activo_afectado (
    id          INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(150) NOT NULL,
    tipo        VARCHAR(80)  NOT NULL,
    descripcion VARCHAR(300),
    CONSTRAINT pk_activo PRIMARY KEY (id)
) ENGINE = InnoDB
  COMMENT = 'Activos de información del banco';

-- ------------------------------------------------------------
-- 1.7 incidente
--     Entidad central del sistema. Referencia al analista
--     asignado y al supervisor son opcionales hasta que
--     ocurra la asignación/escalada respectiva.
-- ------------------------------------------------------------
CREATE TABLE incidente (
    id                    INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    tipo_id               TINYINT UNSIGNED NOT NULL,
    severidad_id          TINYINT UNSIGNED NOT NULL,
    estado_id             TINYINT UNSIGNED NOT NULL,
    activo_id             INT UNSIGNED     NOT NULL,
    analista_id           INT UNSIGNED,
    supervisor_id         INT UNSIGNED,
    descripcion           TEXT             NOT NULL,
    fecha_deteccion       DATETIME         NOT NULL,
    fecha_asignacion      DATETIME,
    fecha_cierre          DATETIME,
    fecha_vencimiento_sla DATETIME         NOT NULL,
    motivo_escalada       VARCHAR(300),
    CONSTRAINT pk_incidente      PRIMARY KEY (id),
    CONSTRAINT fk_inc_tipo       FOREIGN KEY (tipo_id)      REFERENCES tipo_incidente (id)   ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_inc_severidad  FOREIGN KEY (severidad_id) REFERENCES severidad (id)        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_inc_estado     FOREIGN KEY (estado_id)    REFERENCES estado_incidente (id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_inc_activo     FOREIGN KEY (activo_id)    REFERENCES activo_afectado (id)  ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_inc_analista   FOREIGN KEY (analista_id)  REFERENCES usuario (id)          ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_inc_supervisor FOREIGN KEY (supervisor_id) REFERENCES usuario (id)         ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE = InnoDB
  COMMENT = 'Incidentes de seguridad – entidad central del sistema';

-- ------------------------------------------------------------
-- 1.8 evidencia
--     Observaciones y evidencias registradas sobre un incidente.
--     Composición fuerte: si se elimina el incidente, se
--     eliminan sus evidencias.
-- ------------------------------------------------------------
CREATE TABLE evidencia (
    id             INT UNSIGNED NOT NULL AUTO_INCREMENT,
    incidente_id   INT UNSIGNED NOT NULL,
    analista_id    INT UNSIGNED NOT NULL,
    tipo_evidencia VARCHAR(80)  NOT NULL,
    descripcion    TEXT         NOT NULL,
    fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_evidencia    PRIMARY KEY (id),
    CONSTRAINT fk_ev_incidente FOREIGN KEY (incidente_id) REFERENCES incidente (id) ON DELETE CASCADE,
    CONSTRAINT fk_ev_analista  FOREIGN KEY (analista_id)  REFERENCES usuario (id)   ON DELETE RESTRICT
) ENGINE = InnoDB
  COMMENT = 'Evidencias y observaciones por incidente';

-- ------------------------------------------------------------
-- 1.9 bitacora_auditoria
--     Registro inmutable de todas las acciones sobre incidentes.
--     Por diseño: no se aplica UPDATE ni DELETE sobre esta tabla.
-- ------------------------------------------------------------
CREATE TABLE bitacora_auditoria (
    id           INT UNSIGNED NOT NULL AUTO_INCREMENT,
    incidente_id INT UNSIGNED NOT NULL,
    usuario_id   INT UNSIGNED NOT NULL,
    fecha_hora   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accion       VARCHAR(80)  NOT NULL,
    detalle      TEXT,
    CONSTRAINT pk_bitacora    PRIMARY KEY (id),
    CONSTRAINT fk_bit_inc     FOREIGN KEY (incidente_id) REFERENCES incidente (id) ON DELETE RESTRICT,
    CONSTRAINT fk_bit_usuario FOREIGN KEY (usuario_id)  REFERENCES usuario (id)   ON DELETE RESTRICT
) ENGINE = InnoDB
  COMMENT = 'Bitácora de auditoría inmutable – solo INSERT';


-- ============================================================
-- SECCIÓN 2: DML – INSERCIÓN DE DATOS
-- ============================================================

-- 2.1 Tipos de incidente
INSERT INTO tipo_incidente (nombre, descripcion) VALUES
    ('ACCESO_NO_AUTORIZADO',   'Acceso a sistemas o datos sin credenciales válidas'),
    ('MALWARE',                'Infección por software malicioso: ransomware, virus, troyano'),
    ('PHISHING',               'Ataque de ingeniería social para robo de credenciales'),
    ('FUGA_DE_DATOS',          'Exposición o exfiltración de datos confidenciales'),
    ('DENEGACION_DE_SERVICIO', 'Ataque que impide la disponibilidad de un servicio'),
    ('FRAUDE_TRANSACCIONAL',   'Operaciones fraudulentas en canales digitales del banco');

-- 2.2 Niveles de severidad
INSERT INTO severidad (nombre, nivel_prioridad) VALUES
    ('CRITICO', 1),
    ('ALTO',    2),
    ('MEDIO',   3),
    ('BAJO',    4);

-- 2.3 Estados del ciclo de vida
INSERT INTO estado_incidente (nombre) VALUES
    ('DETECTADO'),
    ('EN_ANALISIS'),
    ('CONTENIDO'),
    ('EN_REMEDIACION'),
    ('CERRADO'),
    ('FALSO_POSITIVO');

-- 2.4 Configuración SLA (según RF05)
INSERT INTO configuracion_sla (severidad_id, plazo_horas, descripcion) VALUES
    (1,  4,  'Resolución en menos de 4 horas – Incidente Crítico P1'),
    (2,  8,  'Resolución en menos de 8 horas – Incidente Alto P2'),
    (3, 24,  'Resolución en menos de 24 horas – Incidente Medio P3'),
    (4, 72,  'Resolución en menos de 72 horas – Incidente Bajo P4');

-- 2.5 Usuarios del sistema
INSERT INTO usuario (nombre, apellido, email, legajo, contrasena_hash, rol) VALUES
    ('Martina',  'López',    'mlopez@bancoregio.com.ar',  'SOC-001', SHA2('pass1234',256), 'ANALISTA_SOC'),
    ('Sebastián','Romero',   'sromero@bancoregio.com.ar', 'SOC-002', SHA2('pass5678',256), 'ANALISTA_SOC'),
    ('Andrea',   'Giménez',  'agimenez@bancoregio.com.ar','SUP-001', SHA2('sup@2024',256),  'SUPERVISOR_SEGURIDAD'),
    ('Carlos',   'Ferreyra', 'cferreyra@bancoregio.com.ar','ADM-001',SHA2('adm@2024',256),  'ADMINISTRADOR');

-- 2.6 Activos afectados
INSERT INTO activo_afectado (nombre, tipo, descripcion) VALUES
    ('Servidor de Homebanking',   'Servidor',     'Servidor web de producción del canal homebanking'),
    ('Base de datos de clientes', 'Base de datos','BD Oracle con datos personales y transaccionales'),
    ('VPN corporativa',           'Red',          'Infraestructura VPN de acceso remoto para empleados'),
    ('App Banca Móvil',           'Aplicación',   'Aplicación móvil iOS/Android de Banco Regional');

-- 2.7 Incidentes de ejemplo
INSERT INTO incidente
    (tipo_id, severidad_id, estado_id, activo_id, analista_id, supervisor_id,
     descripcion, fecha_deteccion, fecha_asignacion, fecha_vencimiento_sla) VALUES
    -- Incidente 1: ransomware crítico, en análisis, asignado a Martina López (id=1)
    (2, 1, 2, 1, 1, 3,
     'Detección de proceso ransomware activo en servidor de homebanking. Cifrado parcial de archivos de sesión.',
     '2026-05-08 09:15:00', '2026-05-08 09:32:00',
     DATE_ADD('2026-05-08 09:15:00', INTERVAL 4 HOUR)),

    -- Incidente 2: phishing alto, detectado, sin asignar
    (3, 2, 1, 4, NULL, NULL,
     'Campaña de phishing dirigida a clientes. Correos masivos suplantando identidad del banco con URL maliciosa.',
     '2026-05-09 14:00:00', NULL,
     DATE_ADD('2026-05-09 14:00:00', INTERVAL 8 HOUR)),

    -- Incidente 3: acceso no autorizado medio, cerrado, asignado a Sebastián Romero (id=2)
    (1, 3, 5, 3, 2, NULL,
     'Intento de acceso fallido reiterado a VPN corporativa desde IP extranjera. Sin compromiso confirmado.',
     '2026-05-07 18:00:00', '2026-05-07 18:20:00',
     DATE_ADD('2026-05-07 18:00:00', INTERVAL 24 HOUR));

-- Actualizar fecha_cierre del incidente 3 (cerrado)
UPDATE incidente
SET fecha_cierre = '2026-05-07 22:45:00'
WHERE id = 3;

-- 2.8 Evidencias del incidente 1
INSERT INTO evidencia (incidente_id, analista_id, tipo_evidencia, descripcion, fecha_registro) VALUES
    (1, 1, 'Log del sistema',  'Logs de acceso del servidor muestran proceso "svchost32.exe" no autorizado ejecutándose desde /tmp', '2026-05-08 09:45:00'),
    (1, 1, 'Captura de tráfico', 'Tráfico saliente inusual hacia IP 185.220.101.x en puerto 443, posible C2', '2026-05-08 10:10:00');

-- 2.9 Bitácora de auditoría
INSERT INTO bitacora_auditoria (incidente_id, usuario_id, fecha_hora, accion, detalle) VALUES
    (1, 1, '2026-05-08 09:15:00', 'REGISTRO',    'Incidente registrado con severidad CRITICO por Martina López'),
    (1, 3, '2026-05-08 09:32:00', 'ASIGNACION',  'Incidente asignado a analista Martina López por Andrea Giménez'),
    (1, 1, '2026-05-08 09:40:00', 'CAMBIO_ESTADO','Transición de DETECTADO a EN_ANALISIS'),
    (2, 1, '2026-05-09 14:00:00', 'REGISTRO',    'Incidente registrado con severidad ALTO por Martina López'),
    (3, 2, '2026-05-07 18:00:00', 'REGISTRO',    'Incidente registrado con severidad MEDIO por Sebastián Romero'),
    (3, 2, '2026-05-07 22:45:00', 'CAMBIO_ESTADO','Transición a CERRADO – sin compromiso confirmado tras análisis forense');


-- ============================================================
-- SECCIÓN 3: CONSULTAS SQL (RF07 y RF08)
-- ============================================================

-- 3.1 Listar incidentes abiertos ordenados por prioridad (más crítico primero)
SELECT
    i.id,
    ti.nombre                   AS tipo,
    s.nombre                    AS severidad,
    ei.nombre                   AS estado,
    aa.nombre                   AS activo_afectado,
    CONCAT(u.nombre,' ',u.apellido) AS analista,
    i.fecha_deteccion,
    i.fecha_vencimiento_sla,
    CASE
        WHEN i.fecha_vencimiento_sla < NOW() AND ei.nombre != 'CERRADO' THEN 'VENCIDO'
        WHEN TIMESTAMPDIFF(HOUR, NOW(), i.fecha_vencimiento_sla) <= 1   THEN 'PROXIMO'
        ELSE 'EN_PLAZO'
    END                         AS estado_sla
FROM incidente i
    JOIN tipo_incidente  ti ON ti.id = i.tipo_id
    JOIN severidad       s  ON s.id  = i.severidad_id
    JOIN estado_incidente ei ON ei.id = i.estado_id
    JOIN activo_afectado aa ON aa.id = i.activo_id
    LEFT JOIN usuario    u  ON u.id  = i.analista_id
WHERE ei.nombre NOT IN ('CERRADO', 'FALSO_POSITIVO')
ORDER BY s.nivel_prioridad ASC, i.fecha_deteccion ASC;

-- 3.2 Incidentes con SLA vencido (alerta para el Supervisor)
SELECT
    i.id,
    s.nombre          AS severidad,
    ei.nombre         AS estado,
    i.fecha_deteccion,
    i.fecha_vencimiento_sla,
    TIMESTAMPDIFF(HOUR, i.fecha_vencimiento_sla, NOW()) AS horas_de_retraso,
    CONCAT(u.nombre,' ',u.apellido)                     AS analista_asignado
FROM incidente i
    JOIN severidad       s  ON s.id  = i.severidad_id
    JOIN estado_incidente ei ON ei.id = i.estado_id
    LEFT JOIN usuario    u  ON u.id  = i.analista_id
WHERE i.fecha_vencimiento_sla < NOW()
  AND ei.nombre NOT IN ('CERRADO', 'FALSO_POSITIVO')
ORDER BY horas_de_retraso DESC;

-- 3.3 Cálculo de MTTD promedio (Mean Time To Detect)
--     MTTD = tiempo entre ocurrencia real y registro en sistema.
--     En el SGISI, fecha_deteccion es el momento del registro;
--     se asume igual a la detección real (simplificación del prototipo).
--     Aquí se calcula como promedio entre detección y asignación.
SELECT
    s.nombre AS severidad,
    COUNT(i.id)                                            AS total_incidentes,
    ROUND(AVG(TIMESTAMPDIFF(MINUTE, i.fecha_deteccion, i.fecha_asignacion)), 1)
                                                           AS mttd_promedio_minutos
FROM incidente i
    JOIN severidad s ON s.id = i.severidad_id
WHERE i.fecha_asignacion IS NOT NULL
GROUP BY s.id, s.nombre
ORDER BY s.nivel_prioridad;

-- 3.4 Cálculo de MTTR promedio (Mean Time To Resolve)
SELECT
    s.nombre AS severidad,
    COUNT(i.id)                                            AS incidentes_cerrados,
    ROUND(AVG(TIMESTAMPDIFF(HOUR, i.fecha_deteccion, i.fecha_cierre)), 2)
                                                           AS mttr_promedio_horas,
    cs.plazo_horas                                         AS sla_horas
FROM incidente i
    JOIN severidad         s  ON s.id = i.severidad_id
    JOIN estado_incidente  ei ON ei.id = i.estado_id AND ei.nombre = 'CERRADO'
    JOIN configuracion_sla cs ON cs.severidad_id = s.id
WHERE i.fecha_cierre IS NOT NULL
GROUP BY s.id, s.nombre, cs.plazo_horas
ORDER BY s.nivel_prioridad;

-- 3.5 Distribución de carga de trabajo por analista
SELECT
    CONCAT(u.nombre,' ',u.apellido) AS analista,
    COUNT(i.id)                     AS total_asignados,
    SUM(CASE WHEN ei.nombre NOT IN ('CERRADO','FALSO_POSITIVO') THEN 1 ELSE 0 END) AS activos,
    SUM(CASE WHEN ei.nombre = 'CERRADO'       THEN 1 ELSE 0 END) AS cerrados
FROM usuario u
    JOIN incidente       i  ON i.analista_id  = u.id
    JOIN estado_incidente ei ON ei.id = i.estado_id
WHERE u.rol = 'ANALISTA_SOC'
GROUP BY u.id, u.nombre, u.apellido
ORDER BY activos DESC;

-- 3.6 Historial de auditoría de un incidente específico (incidente id=1)
SELECT
    ba.fecha_hora,
    ba.accion,
    CONCAT(u.nombre,' ',u.apellido) AS ejecutado_por,
    ba.detalle
FROM bitacora_auditoria ba
    JOIN usuario u ON u.id = ba.usuario_id
WHERE ba.incidente_id = 1
ORDER BY ba.fecha_hora ASC;

-- 3.7 Filtro combinado: incidentes por tipo + severidad + rango de fechas (RF07)
SELECT
    i.id,
    ti.nombre  AS tipo,
    s.nombre   AS severidad,
    ei.nombre  AS estado,
    i.fecha_deteccion
FROM incidente i
    JOIN tipo_incidente   ti ON ti.id = i.tipo_id
    JOIN severidad        s  ON s.id  = i.severidad_id
    JOIN estado_incidente ei ON ei.id = i.estado_id
WHERE ti.nombre    = 'MALWARE'
  AND s.nombre     = 'CRITICO'
  AND i.fecha_deteccion BETWEEN '2026-05-01' AND '2026-05-31'
ORDER BY i.fecha_deteccion DESC;


-- ============================================================
-- SECCIÓN 4: DML – BORRADO DE REGISTROS
-- ============================================================

-- 4.1 Eliminar una evidencia puntual (por id)
--     Caso de uso: evidencia duplicada o cargada por error.
DELETE FROM evidencia
WHERE id = 2;

-- 4.2 Baja lógica de un usuario (no se elimina físicamente)
--     El historial de bitácora y asignaciones se preserva.
UPDATE usuario
SET activo = 0
WHERE id = 2;

-- 4.3 Eliminar un incidente en estado FALSO_POSITIVO
--     Solo se permite borrar incidentes descartados; la bitácora
--     usa ON DELETE RESTRICT, por lo que primero se eliminan
--     sus evidencias y luego el incidente (si la bitácora lo permite).
--     NOTA: En producción esta operación se realiza por el Administrador.
DELETE FROM evidencia WHERE incidente_id = 3;
-- La bitácora tiene ON DELETE RESTRICT, por lo que el incidente
-- no puede eliminarse si tiene entradas. En producción se usa baja lógica:
UPDATE incidente SET estado_id = 6 WHERE id = 3;  -- estado_id 6 = FALSO_POSITIVO

-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================
