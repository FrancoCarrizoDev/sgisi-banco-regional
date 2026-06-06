package com.bancoregio.sgisi.servicio;

import com.bancoregio.sgisi.dao.CatalogoDAO;
import com.bancoregio.sgisi.modelo.ActivoAfectado;
import com.bancoregio.sgisi.modelo.NivelSeveridad;
import com.bancoregio.sgisi.modelo.TipoIncidente;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio de lectura de catálogos.
 *
 * Los catálogos son datos de apoyo para formularios y filtros: tipos de
 * incidente, severidades y activos afectados. Esta capa mantiene a la UI
 * separada del DAO aunque por ahora solo delegue consultas simples.
 */
public class CatalogoService {
    private final CatalogoDAO dao;

    public CatalogoService(CatalogoDAO dao) {
        this.dao = dao;
    }

    public List<TipoIncidente> listarTipos() throws SQLException {
        return dao.listarTipos();
    }

    public List<NivelSeveridad> listarSeveridades() throws SQLException {
        return dao.listarSeveridades();
    }

    public List<ActivoAfectado> listarActivos() throws SQLException {
        return dao.listarActivos();
    }
}
