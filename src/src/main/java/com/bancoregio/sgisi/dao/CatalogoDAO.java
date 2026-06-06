package com.bancoregio.sgisi.dao;

import com.bancoregio.sgisi.modelo.ActivoAfectado;
import com.bancoregio.sgisi.modelo.NivelSeveridad;
import com.bancoregio.sgisi.modelo.TipoIncidente;

import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de acceso a datos para catálogos del sistema.
 *
 * Los catálogos alimentan formularios y filtros, por eso se consultan como listas
 * de lectura sin reglas de negocio complejas.
 */
public interface CatalogoDAO {
    List<TipoIncidente> listarTipos() throws SQLException;

    List<NivelSeveridad> listarSeveridades() throws SQLException;

    List<ActivoAfectado> listarActivos() throws SQLException;
}
