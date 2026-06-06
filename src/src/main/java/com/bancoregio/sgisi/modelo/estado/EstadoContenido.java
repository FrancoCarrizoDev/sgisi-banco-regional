package com.bancoregio.sgisi.modelo.estado;

import java.util.Set;

/**
 * Estado donde el incidente ya fue contenido y se evita que siga propagándose.
 */
public class EstadoContenido extends EstadoIncidenteState {
    public String getNombre() {
        return "CONTENIDO";
    }

    public Set<String> getTransicionesValidas() {
        return Set.of("EN_REMEDIACION", "FALSO_POSITIVO");
    }
}
