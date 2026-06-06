package com.bancoregio.sgisi.modelo;

/**
 * Catálogo que indica la criticidad del incidente y su prioridad relativa.
 */
public record NivelSeveridad(int id, String nombre, int nivelPrioridad) {
}
