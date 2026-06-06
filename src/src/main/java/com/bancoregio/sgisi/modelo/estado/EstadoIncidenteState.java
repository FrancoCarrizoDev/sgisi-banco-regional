package com.bancoregio.sgisi.modelo.estado;

import java.util.Set;

/**
 * Base del patrón State para el ciclo de vida de incidentes.
 *
 * Cada subclase representa un estado real del negocio y declara a qué estados se
 * puede avanzar. Así la validación de transiciones queda cerca del estado actual
 * y no repartida en if/switch dentro del servicio o de la interfaz.
 */
public abstract class EstadoIncidenteState {
    public abstract String getNombre();

    /**
     * Define los estados destino permitidos desde este punto del proceso.
     */
    public abstract Set<String> getTransicionesValidas();

    /**
     * Punto común de validación usado por la capa de servicio antes de persistir
     * un cambio de estado.
     */
    public boolean puedeTransicionarA(String destino) {
        return getTransicionesValidas().contains(destino);
    }
}
