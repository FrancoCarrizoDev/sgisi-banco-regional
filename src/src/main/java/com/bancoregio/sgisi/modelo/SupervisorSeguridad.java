package com.bancoregio.sgisi.modelo;

/**
 * Especialización de Usuario para perfiles que supervisan la gestión de seguridad.
 */
public class SupervisorSeguridad extends Usuario {
    public SupervisorSeguridad(int id, String n, String a, String e) {
        super(id, n, a, e, "SUPERVISOR_SEGURIDAD");
    }
}
