package com.bancoregio.sgisi.modelo;

/**
 * Usuario con rol supervisor de seguridad.
 */
public class SupervisorSeguridad extends Usuario {
    /**
     * Crea supervisor.
     */
    public SupervisorSeguridad(int id, String n, String a, String e) {
        super(id, n, a, e, "SUPERVISOR_SEGURIDAD");
    }
}
