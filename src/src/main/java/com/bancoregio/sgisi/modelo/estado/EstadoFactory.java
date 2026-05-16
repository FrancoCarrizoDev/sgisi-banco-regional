package com.bancoregio.sgisi.modelo.estado;

/**
 * Fábrica para reconstruir estados persistidos.
 */
public final class EstadoFactory {
    private EstadoFactory() {
    }

    /**
     * Crea estado por nombre persistido.
     */
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
