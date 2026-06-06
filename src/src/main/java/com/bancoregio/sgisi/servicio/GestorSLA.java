package com.bancoregio.sgisi.servicio;

import com.bancoregio.sgisi.dao.SlaDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Componente de negocio encargado del SLA.
 *
 * Traduce la severidad de un incidente en una fecha límite de atención. El plazo
 * no está fijo en código: se obtiene desde la tabla de configuración de SLA para
 * que el criterio pueda cambiar sin modificar la aplicación.
 */
public class GestorSLA {
    private final SlaDAO slaDAO;

    public GestorSLA(SlaDAO slaDAO) {
        this.slaDAO = slaDAO;
    }

    /**
     * Calcula vencimiento para severidad y fecha base.
     */
    public LocalDateTime calcularVencimiento(int severidadId, LocalDateTime base) throws SQLException {
        var sla = slaDAO.buscarPorSeveridad(severidadId).orElseThrow(() -> new IllegalArgumentException("Sin SLA para severidad"));
        return base.plusHours(sla.plazoHoras());
    }
}
