package com.bancoregio.sgisi.modelo;

/**
 * Especialización de Usuario para perfiles administradores del sistema.
 */
public class Administrador extends Usuario {
    public Administrador(int id, String n, String a, String e) {
        super(id, n, a, e, "ADMINISTRADOR");
    }
}
