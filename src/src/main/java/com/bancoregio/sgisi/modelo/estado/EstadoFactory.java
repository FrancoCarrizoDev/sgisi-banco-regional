package com.bancoregio.sgisi.modelo.estado;

/**
 * Fábrica para reconstruir objetos de estado desde la base de datos.
 *
 * En la tabla se guarda el nombre del estado, pero en memoria el incidente usa
 * objetos que conocen sus transiciones. Esta clase traduce el valor persistido al
 * objeto correspondiente del patrón State.
 */
public final class EstadoFactory {
    private EstadoFactory() {
    }

    public static EstadoIncidenteState crear(String nombre) {
        return switch (nombre) {
            case "DETECTADO" -> new EstadoDetectado();
            case "EN_ANALISIS" -> new EstadoEnAnalisis();
            case "CONTENIDO" -> new EstadoContenido();
            case "EN_REMEDIACION" -> new EstadoEnRemediacion();
            case "CERRADO" -> new EstadoCerrado();
            case "FALSO_POSITIVO" -> new EstadoFalsoPositivo();
            default -> throw new IllegalArgumentException("Estado no soportado: " + nombre);
        };
    }
}
