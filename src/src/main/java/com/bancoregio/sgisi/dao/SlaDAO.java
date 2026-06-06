package com.bancoregio.sgisi.dao;

import com.bancoregio.sgisi.modelo.ConfiguracionSLA;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Contrato de acceso a datos para la configuración de SLA.
 */
public interface SlaDAO {
    /**
     * Obtiene el plazo aplicable a una severidad para que el servicio calcule la
     * fecha de vencimiento del incidente.
     */
    Optional<ConfiguracionSLA> buscarPorSeveridad(int severidadId) throws SQLException;
}
