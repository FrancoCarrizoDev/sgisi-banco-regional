package com.bancoregio.sgisi.dao.impl;

import com.bancoregio.sgisi.dao.CatalogoDAO;
import com.bancoregio.sgisi.modelo.ActivoAfectado;
import com.bancoregio.sgisi.modelo.NivelSeveridad;
import com.bancoregio.sgisi.modelo.TipoIncidente;
import com.bancoregio.sgisi.util.ConexionDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de catálogos.
 *
 * Lee tablas maestras usadas por la interfaz. Como son datos simples de apoyo,
 * cada método ejecuta una consulta directa y transforma cada fila en un record
 * inmutable del modelo.
 */
public class CatalogoJDBC implements CatalogoDAO {
    public List<TipoIncidente> listarTipos() throws SQLException {
        List<TipoIncidente> out = new ArrayList<>();
        try (var c = ConexionDB.getInstance().getConnection(); var ps = c.prepareStatement("SELECT id,nombre,descripcion FROM tipo_incidente ORDER BY nombre"); var rs = ps.executeQuery()) {
            while (rs.next()) out.add(new TipoIncidente(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return out;
    }

    public List<NivelSeveridad> listarSeveridades() throws SQLException {
        List<NivelSeveridad> out = new ArrayList<>();
        try (var c = ConexionDB.getInstance().getConnection(); var ps = c.prepareStatement("SELECT id,nombre,nivel_prioridad FROM severidad ORDER BY nivel_prioridad"); var rs = ps.executeQuery()) {
            while (rs.next()) out.add(new NivelSeveridad(rs.getInt(1), rs.getString(2), rs.getInt(3)));
        }
        return out;
    }

    public List<ActivoAfectado> listarActivos() throws SQLException {
        List<ActivoAfectado> out = new ArrayList<>();
        try (var c = ConexionDB.getInstance().getConnection(); var ps = c.prepareStatement("SELECT id,nombre,tipo,descripcion FROM activo_afectado ORDER BY nombre"); var rs = ps.executeQuery()) {
            while (rs.next())
                out.add(new ActivoAfectado(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        return out;
    }
}
