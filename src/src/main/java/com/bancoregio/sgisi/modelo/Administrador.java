package com.bancoregio.sgisi.modelo;

/**
 * Usuario con rol administrador.
 */
public class Administrador extends Usuario {
    /**
     * Crea administrador.
     */
    public Administrador(int id, String n, String a, String e) {
        super(id, n, a, e, "ADMINISTRADOR");
    }
}
