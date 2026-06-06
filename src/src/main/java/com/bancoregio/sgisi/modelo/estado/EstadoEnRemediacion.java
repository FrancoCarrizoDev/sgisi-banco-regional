package com.bancoregio.sgisi.modelo.estado;

import java.util.Set;

/**
 * Estado donde se corrige la causa raíz y se aplican acciones de recuperación.
 */
public class EstadoEnRemediacion extends EstadoIncidenteState {
    public String getNombre() {
        return "EN_REMEDIACION";
    }

    public Set<String> getTransicionesValidas() {
        return Set.of("CERRADO", "FALSO_POSITIVO");
    }
}
