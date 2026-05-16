package com.bancoregio.sgisi.modelo;

import java.time.LocalDateTime;

/**
 * Entrada de bitácora de auditoría.
 */
public record EntradaBitacora(Integer id, int incidenteId, int usuarioId, LocalDateTime fechaHora, String accion,
                              String detalle) {
}
