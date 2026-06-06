package com.bancoregio.sgisi.modelo;

/**
 * Catálogo que clasifica la naturaleza del incidente reportado.
 */
public record TipoIncidente(int id, String nombre, String descripcion) {
}
