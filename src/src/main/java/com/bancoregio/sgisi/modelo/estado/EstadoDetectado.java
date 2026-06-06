package com.bancoregio.sgisi.modelo.estado;

import java.util.Set;

/**
 * Estado inicial: el incidente fue reportado, pero todavía no se analizó.
 */
public class EstadoDetectado extends EstadoIncidenteState {
    public String getNombre() {
        return "DETECTADO";
    }

    public Set<String> getTransicionesValidas() {
        return Set.of("EN_ANALISIS", "FALSO_POSITIVO");
    }
}
