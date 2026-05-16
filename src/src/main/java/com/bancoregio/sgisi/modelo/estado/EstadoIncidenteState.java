package com.bancoregio.sgisi.modelo.estado;

import java.util.Set;

/** Contrato de estados del ciclo de vida de incidentes. */
public abstract class EstadoIncidenteState {
    /** Devuelve nombre persistido del estado. */
    public abstract String getNombre();
    /** Devuelve los estados destino válidos. */
    public abstract Set<String> getTransicionesValidas();
    /** Verifica transición permitida. */
    public boolean puedeTransicionarA(String destino){ return getTransicionesValidas().contains(destino); }
}
