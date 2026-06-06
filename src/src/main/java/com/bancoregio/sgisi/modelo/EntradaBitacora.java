package com.bancoregio.sgisi.modelo;

import java.time.LocalDateTime;

/**
 * Registro de auditoría asociado a un incidente.
 *
 * Permite reconstruir qué usuario realizó una acción, cuándo la hizo y con qué
 * detalle operativo.
 */
public record EntradaBitacora(Integer id, int incidenteId, int usuarioId, LocalDateTime fechaHora, String accion,
                              String detalle) {
}
