package com.bancoregio.sgisi.modelo;
/** Configuración SLA por severidad. */
public record ConfiguracionSLA(int id, int severidadId, int plazoHoras, String descripcion) {}
