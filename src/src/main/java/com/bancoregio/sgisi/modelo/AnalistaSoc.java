package com.bancoregio.sgisi.modelo;

/**
 * Especialización de Usuario para operadores que registran y gestionan incidentes.
 */
public class AnalistaSoc extends Usuario {
    public AnalistaSoc(int id, String n, String a, String e) {
        super(id, n, a, e, "ANALISTA_SOC");
    }
}
