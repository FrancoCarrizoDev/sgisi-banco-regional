package com.bancoregio.sgisi.modelo;

/**
 * Parámetro de negocio que define el plazo máximo de atención según severidad.
 */
public record ConfiguracionSLA(int id, int severidadId, int plazoHoras, String descripcion) {
}
