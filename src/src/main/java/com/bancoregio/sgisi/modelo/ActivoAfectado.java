package com.bancoregio.sgisi.modelo;

/**
 * Activo de información comprometido o impactado por un incidente.
 */
public record ActivoAfectado(int id, String nombre, String tipo, String descripcion) {
}
