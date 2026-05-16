package com.bancoregio.sgisi.servicio;

import com.bancoregio.sgisi.dao.SlaDAO;
import java.sql.SQLException;
import java.time.LocalDateTime;

/** Calcula fecha de vencimiento SLA. */
public class GestorSLA {
    private final SlaDAO slaDAO;
    /** Crea gestor SLA. */
    public GestorSLA(SlaDAO slaDAO){ this.slaDAO=slaDAO; }
    /** Calcula vencimiento para severidad y fecha base. */
    public LocalDateTime calcularVencimiento(int severidadId, LocalDateTime base) throws SQLException { var sla=slaDAO.buscarPorSeveridad(severidadId).orElseThrow(() -> new IllegalArgumentException("Sin SLA para severidad")); return base.plusHours(sla.plazoHoras()); }
}
