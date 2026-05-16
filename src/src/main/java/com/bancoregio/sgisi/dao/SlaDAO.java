package com.bancoregio.sgisi.dao;

import com.bancoregio.sgisi.modelo.ConfiguracionSLA;

import java.sql.SQLException;
import java.util.Optional;

/**
 * DAO de SLA.
 */
public interface SlaDAO {
    /**
     * Busca SLA por severidad.
     */
    Optional<ConfiguracionSLA> buscarPorSeveridad(int severidadId) throws SQLException;
}
