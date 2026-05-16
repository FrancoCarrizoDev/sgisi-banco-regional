package com.bancoregio.sgisi.servicio;

import com.bancoregio.sgisi.dao.CatalogoDAO;
import com.bancoregio.sgisi.modelo.ActivoAfectado;
import com.bancoregio.sgisi.modelo.NivelSeveridad;
import com.bancoregio.sgisi.modelo.TipoIncidente;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio de lectura de catálogos.
 */
public class CatalogoService {
    private final CatalogoDAO dao;

    /**
     * Crea servicio de catálogos.
     */
    public CatalogoService(CatalogoDAO dao) {
        this.dao = dao;
    }

    /**
     * Lista tipos de incidente.
     */
    public List<TipoIncidente> listarTipos() throws SQLException {
        return dao.listarTipos();
    }

    /**
     * Lista severidades.
     */
    public List<NivelSeveridad> listarSeveridades() throws SQLException {
        return dao.listarSeveridades();
    }

    /**
     * Lista activos afectados.
     */
    public List<ActivoAfectado> listarActivos() throws SQLException {
        return dao.listarActivos();
    }
}
