package com.bancoregio.sgisi.dao;

import com.bancoregio.sgisi.modelo.*;
import java.sql.SQLException;
import java.util.List;

/** DAO de catálogos. */
public interface CatalogoDAO {
    /** Lista tipos de incidente. */ List<TipoIncidente> listarTipos() throws SQLException;
    /** Lista severidades. */ List<NivelSeveridad> listarSeveridades() throws SQLException;
    /** Lista activos afectados. */ List<ActivoAfectado> listarActivos() throws SQLException;
}
