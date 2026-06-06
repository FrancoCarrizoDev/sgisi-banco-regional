package com.bancoregio.sgisi.dao.impl;

import com.bancoregio.sgisi.dao.BitacoraDAO;
import com.bancoregio.sgisi.modelo.EntradaBitacora;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Implementación JDBC de la bitácora de auditoría.
 *
 * Se usa desde los servicios para dejar trazabilidad de acciones importantes,
 * como registrar incidentes o cambiar estados.
 */
public class BitacoraJDBC implements BitacoraDAO {
    public void insertar(EntradaBitacora e, Connection c) throws SQLException {
        try (var ps = c.prepareStatement("INSERT INTO bitacora_auditoria(incidente_id,usuario_id,fecha_hora,accion,detalle) VALUES(?,?,?,?,?)")) {
            ps.setInt(1, e.incidenteId());
            ps.setInt(2, e.usuarioId());
            ps.setTimestamp(3, Timestamp.valueOf(e.fechaHora()));
            ps.setString(4, e.accion());
            ps.setString(5, e.detalle());
            ps.executeUpdate();
        }
    }
}
