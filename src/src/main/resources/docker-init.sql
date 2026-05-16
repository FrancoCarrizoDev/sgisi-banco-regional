SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS sgisi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sgisi;

CREATE TABLE IF NOT EXISTS tipo_incidente (
  id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  descripcion VARCHAR(200) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_tipo_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS severidad (
  id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(20) NOT NULL,
  nivel_prioridad TINYINT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_severidad_nombre (nombre),
  UNIQUE KEY uq_severidad_nivel (nivel_prioridad)
);

CREATE TABLE IF NOT EXISTS estado_incidente (
  id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(30) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_estado_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS configuracion_sla (
  id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  severidad_id TINYINT UNSIGNED NOT NULL,
  plazo_horas SMALLINT UNSIGNED NOT NULL,
  descripcion VARCHAR(100),
  PRIMARY KEY (id),
  UNIQUE KEY uq_sla_severidad (severidad_id),
  CONSTRAINT fk_sla_severidad FOREIGN KEY (severidad_id) REFERENCES severidad(id)
);

CREATE TABLE IF NOT EXISTS usuario (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL,
  legajo VARCHAR(20),
  contrasena_hash VARCHAR(255) NOT NULL,
  rol ENUM('ANALISTA_SOC','SUPERVISOR_SEGURIDAD','ADMINISTRADOR') NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_email (email),
  UNIQUE KEY uq_legajo (legajo)
);

CREATE TABLE IF NOT EXISTS activo_afectado (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  tipo VARCHAR(80) NOT NULL,
  descripcion VARCHAR(300),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS incidente (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  tipo_id TINYINT UNSIGNED NOT NULL,
  severidad_id TINYINT UNSIGNED NOT NULL,
  estado_id TINYINT UNSIGNED NOT NULL,
  activo_id INT UNSIGNED NOT NULL,
  analista_id INT UNSIGNED NULL,
  supervisor_id INT UNSIGNED NULL,
  descripcion TEXT NOT NULL,
  fecha_deteccion DATETIME NOT NULL,
  fecha_asignacion DATETIME NULL,
  fecha_cierre DATETIME NULL,
  fecha_vencimiento_sla DATETIME NOT NULL,
  motivo_escalada VARCHAR(300) NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_inc_tipo FOREIGN KEY (tipo_id) REFERENCES tipo_incidente(id),
  CONSTRAINT fk_inc_severidad FOREIGN KEY (severidad_id) REFERENCES severidad(id),
  CONSTRAINT fk_inc_estado FOREIGN KEY (estado_id) REFERENCES estado_incidente(id),
  CONSTRAINT fk_inc_activo FOREIGN KEY (activo_id) REFERENCES activo_afectado(id),
  CONSTRAINT fk_inc_analista FOREIGN KEY (analista_id) REFERENCES usuario(id),
  CONSTRAINT fk_inc_supervisor FOREIGN KEY (supervisor_id) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS bitacora_auditoria (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  incidente_id INT UNSIGNED NOT NULL,
  usuario_id INT UNSIGNED NOT NULL,
  fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  accion VARCHAR(80) NOT NULL,
  detalle TEXT,
  PRIMARY KEY (id),
  CONSTRAINT fk_bit_inc FOREIGN KEY (incidente_id) REFERENCES incidente(id) ON DELETE RESTRICT,
  CONSTRAINT fk_bit_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE RESTRICT
);

INSERT IGNORE INTO tipo_incidente (id, nombre, descripcion) VALUES
 (1,'ACCESO_NO_AUTORIZADO','Acceso sin credenciales'),
 (2,'MALWARE','Infección por software malicioso'),
 (3,'PHISHING','Ingeniería social'),
 (4,'FUGA_DE_DATOS','Exposición de datos'),
 (5,'DENEGACION_DE_SERVICIO','No disponibilidad'),
 (6,'FRAUDE_TRANSACCIONAL','Fraude digital');

INSERT IGNORE INTO severidad (id,nombre,nivel_prioridad) VALUES
 (1,'CRITICO',1),(2,'ALTO',2),(3,'MEDIO',3),(4,'BAJO',4);

INSERT IGNORE INTO estado_incidente (id,nombre) VALUES
 (1,'DETECTADO'),(2,'EN_ANALISIS'),(3,'CONTENIDO'),(4,'EN_REMEDIACION'),(5,'CERRADO'),(6,'FALSO_POSITIVO');

INSERT IGNORE INTO configuracion_sla (id,severidad_id,plazo_horas,descripcion) VALUES
 (1,1,4,'SLA CRITICO'),(2,2,8,'SLA ALTO'),(3,3,24,'SLA MEDIO'),(4,4,72,'SLA BAJO');

INSERT IGNORE INTO usuario (id,nombre,apellido,email,legajo,contrasena_hash,rol,activo) VALUES
 (1,'Martina','López','mlopez@bancoregio.com.ar','SOC-001',SHA2('pass1234',256),'ANALISTA_SOC',1),
 (2,'Sebastián','Romero','sromero@bancoregio.com.ar','SOC-002',SHA2('pass5678',256),'ANALISTA_SOC',1),
 (3,'Andrea','Giménez','agimenez@bancoregio.com.ar','SUP-001',SHA2('sup@2024',256),'SUPERVISOR_SEGURIDAD',1),
 (4,'Carlos','Ferreyra','cferreyra@bancoregio.com.ar','ADM-001',SHA2('adm@2024',256),'ADMINISTRADOR',1);

INSERT IGNORE INTO activo_afectado (id,nombre,tipo,descripcion) VALUES
 (1,'Servidor de Homebanking','Servidor','Servidor web de producción'),
 (2,'Base de datos de clientes','Base de datos','Datos personales y transaccionales'),
 (3,'VPN corporativa','Red','Infraestructura VPN'),
 (4,'App Banca Móvil','Aplicación','Aplicación iOS/Android');

INSERT IGNORE INTO incidente
  (id,tipo_id,severidad_id,estado_id,activo_id,analista_id,supervisor_id,descripcion,fecha_deteccion,fecha_asignacion,fecha_cierre,fecha_vencimiento_sla,motivo_escalada)
VALUES
  (1,2,1,2,1,1,3,'Detección de malware crítico en el servidor de Homebanking.',DATE_SUB(NOW(), INTERVAL 6 HOUR),DATE_SUB(NOW(), INTERVAL 5 HOUR),NULL,DATE_SUB(NOW(), INTERVAL 2 HOUR),'Escalado por criticidad del activo afectado'),
  (2,1,2,1,3,NULL,NULL,'Intentos reiterados de acceso no autorizado desde una IP externa.',DATE_SUB(NOW(), INTERVAL 3 HOUR),NULL,NULL,DATE_ADD(NOW(), INTERVAL 5 HOUR),NULL),
  (3,3,3,3,4,2,NULL,'Campaña de phishing reportada por usuarios de banca móvil.',DATE_SUB(NOW(), INTERVAL 20 HOUR),DATE_SUB(NOW(), INTERVAL 18 HOUR),NULL,DATE_ADD(NOW(), INTERVAL 4 HOUR),NULL),
  (4,4,1,4,2,1,3,'Posible exposición de datos personales en consulta interna.',DATE_SUB(NOW(), INTERVAL 10 HOUR),DATE_SUB(NOW(), INTERVAL 9 HOUR),NULL,DATE_SUB(NOW(), INTERVAL 6 HOUR),'Requiere seguimiento de supervisor'),
  (5,5,2,5,1,2,3,'Pico de tráfico descartado tras mitigación de denegación de servicio.',DATE_SUB(NOW(), INTERVAL 2 DAY),DATE_SUB(NOW(), INTERVAL 47 HOUR),DATE_SUB(NOW(), INTERVAL 36 HOUR),DATE_SUB(NOW(), INTERVAL 40 HOUR),NULL),
  (6,6,3,6,2,1,NULL,'Alerta de fraude transaccional descartada por regla duplicada.',DATE_SUB(NOW(), INTERVAL 1 DAY),DATE_SUB(NOW(), INTERVAL 23 HOUR),DATE_SUB(NOW(), INTERVAL 22 HOUR),DATE_ADD(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 24 HOUR),NULL),
  (7,1,4,1,4,NULL,NULL,'Usuario reporta inicio de sesión sospechoso en aplicación móvil.',DATE_SUB(NOW(), INTERVAL 1 HOUR),NULL,NULL,DATE_ADD(NOW(), INTERVAL 71 HOUR),NULL),
  (8,2,2,2,3,2,3,'Equipo de soporte detecta comportamiento anómalo en conexión VPN.',DATE_SUB(NOW(), INTERVAL 7 HOUR),DATE_SUB(NOW(), INTERVAL 6 HOUR),NULL,DATE_ADD(NOW(), INTERVAL 1 HOUR),NULL);

INSERT IGNORE INTO bitacora_auditoria (id,incidente_id,usuario_id,fecha_hora,accion,detalle) VALUES
  (1,1,1,DATE_SUB(NOW(), INTERVAL 6 HOUR),'REGISTRO','Incidente registrado por monitoreo SOC'),
  (2,1,1,DATE_SUB(NOW(), INTERVAL 5 HOUR),'CAMBIO_ESTADO','Transición DETECTADO a EN_ANALISIS'),
  (3,2,3,DATE_SUB(NOW(), INTERVAL 3 HOUR),'REGISTRO','Incidente creado desde reporte de firewall'),
  (4,3,2,DATE_SUB(NOW(), INTERVAL 20 HOUR),'REGISTRO','Incidente reportado por mesa de ayuda'),
  (5,3,2,DATE_SUB(NOW(), INTERVAL 18 HOUR),'CAMBIO_ESTADO','Transición EN_ANALISIS a CONTENIDO'),
  (6,4,1,DATE_SUB(NOW(), INTERVAL 10 HOUR),'REGISTRO','Incidente registrado por alerta DLP'),
  (7,4,3,DATE_SUB(NOW(), INTERVAL 9 HOUR),'CAMBIO_ESTADO','Transición CONTENIDO a EN_REMEDIACION'),
  (8,5,2,DATE_SUB(NOW(), INTERVAL 2 DAY),'REGISTRO','Incidente registrado por monitoreo de disponibilidad'),
  (9,5,3,DATE_SUB(NOW(), INTERVAL 36 HOUR),'CAMBIO_ESTADO','Incidente cerrado tras mitigación'),
  (10,6,1,DATE_SUB(NOW(), INTERVAL 1 DAY),'REGISTRO','Incidente generado por motor antifraude'),
  (11,6,1,DATE_SUB(NOW(), INTERVAL 22 HOUR),'CAMBIO_ESTADO','Clasificado como falso positivo'),
  (12,7,4,DATE_SUB(NOW(), INTERVAL 1 HOUR),'REGISTRO','Incidente reportado por usuario interno'),
  (13,8,2,DATE_SUB(NOW(), INTERVAL 7 HOUR),'REGISTRO','Incidente registrado desde monitoreo VPN'),
  (14,8,2,DATE_SUB(NOW(), INTERVAL 6 HOUR),'CAMBIO_ESTADO','Transición DETECTADO a EN_ANALISIS');
