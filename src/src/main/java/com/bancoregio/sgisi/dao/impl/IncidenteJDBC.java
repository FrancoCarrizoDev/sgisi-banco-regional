package com.bancoregio.sgisi.dao.impl;

import com.bancoregio.sgisi.dao.IncidenteDAO;
import com.bancoregio.sgisi.modelo.ActivoAfectado;
import com.bancoregio.sgisi.modelo.Incidente;
import com.bancoregio.sgisi.modelo.NivelSeveridad;
import com.bancoregio.sgisi.modelo.TipoIncidente;
import com.bancoregio.sgisi.modelo.estado.EstadoFactory;
import com.bancoregio.sgisi.util.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de incidentes.
 */
public class IncidenteJDBC implements IncidenteDAO {
    /**
     * {@inheritDoc}
     */
    public int insertar(Incidente i, Connection c) throws SQLException {
        String sql = "INSERT INTO incidente(tipo_id,severidad_id,estado_id,activo_id,descripcion,fecha_deteccion,fecha_vencimiento_sla) VALUES(?,?,?,?,?,?,?)";
        try (var ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i.getTipo().id());
            ps.setInt(2, i.getSeveridad().id());
            ps.setInt(3, estadoIdPorNombre(i.getEstado().getNombre(), c));
            ps.setInt(4, i.getActivo().id());
            ps.setString(5, i.getDescripcion());
            ps.setTimestamp(6, Timestamp.valueOf(i.getFechaDeteccion()));
            ps.setTimestamp(7, Timestamp.valueOf(i.getFechaVencimientoSla()));
            ps.executeUpdate();
            try (var gk = ps.getGeneratedKeys()) {
                gk.next();
                return gk.getInt(1);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void actualizarEstado(int incidenteId, int estadoId, LocalDateTime fechaCierre, Connection c) throws SQLException {
        try (var ps = c.prepareStatement("UPDATE incidente SET estado_id=?, fecha_cierre=? WHERE id=?")) {
            ps.setInt(1, estadoId);
            if (fechaCierre == null) ps.setNull(2, Types.TIMESTAMP);
            else ps.setTimestamp(2, Timestamp.valueOf(fechaCierre));
            ps.setInt(3, incidenteId);
            ps.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Optional<Incidente> buscarPorId(int id) throws SQLException {
        List<Incidente> list = listarInterno(" WHERE i.id=? ", ps -> ps.setInt(1, id));
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    /**
     * {@inheritDoc}
     */
    public List<Incidente> listar(Integer estadoId, Integer severidadId) throws SQLException {
        String where = " WHERE 1=1 " + (estadoId != null ? " AND i.estado_id=?" : "") + (severidadId != null ? " AND i.severidad_id=?" : "");
        return listarInterno(where, ps -> {
            int idx = 1;
            if (estadoId != null) ps.setInt(idx++, estadoId);
            if (severidadId != null) ps.setInt(idx, severidadId);
        });
    }

    private List<Incidente> listarInterno(String where, SqlBinder binder) throws SQLException {
        String sql = "SELECT i.id,i.descripcion,i.fecha_deteccion,i.fecha_vencimiento_sla,i.fecha_cierre,ti.id tipo_id,ti.nombre tipo_nombre,ti.descripcion tipo_desc,s.id sev_id,s.nombre sev_nombre,s.nivel_prioridad,a.id act_id,a.nombre act_nombre,a.tipo act_tipo,a.descripcion act_desc,e.nombre estado_nombre FROM incidente i JOIN tipo_incidente ti ON ti.id=i.tipo_id JOIN severidad s ON s.id=i.severidad_id JOIN activo_afectado a ON a.id=i.activo_id JOIN estado_incidente e ON e.id=i.estado_id" + where + " ORDER BY i.fecha_deteccion DESC";
        List<Incidente> out = new ArrayList<>();
        try (var c = ConexionDB.getInstance().getConnection(); var ps = c.prepareStatement(sql)) {
            binder.bind(ps);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Incidente(rs.getInt("id"), new TipoIncidente(rs.getInt("tipo_id"), rs.getString("tipo_nombre"), rs.getString("tipo_desc")), new NivelSeveridad(rs.getInt("sev_id"), rs.getString("sev_nombre"), rs.getInt("nivel_prioridad")), new ActivoAfectado(rs.getInt("act_id"), rs.getString("act_nombre"), rs.getString("act_tipo"), rs.getString("act_desc")), rs.getString("descripcion"), rs.getTimestamp("fecha_deteccion").toLocalDateTime(), rs.getTimestamp("fecha_vencimiento_sla").toLocalDateTime(), rs.getTimestamp("fecha_cierre") != null ? rs.getTimestamp("fecha_cierre").toLocalDateTime() : null, EstadoFactory.crear(rs.getString("estado_nombre"))));
                }
            }
        }
        return out;
    }

    /**
     * Devuelve el id de estado por nombre.
     */
    public int estadoIdPorNombre(String nombre, Connection c) throws SQLException {
        try (var ps = c.prepareStatement("SELECT id FROM estado_incidente WHERE nombre=?")) {
            ps.setString(1, nombre);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Estado no encontrado: " + nombre);
                return rs.getInt(1);
            }
        }
    }

    private interface SqlBinder {
        void bind(PreparedStatement ps) throws SQLException;
    }
}
