package com.bancoregio.sgisi.modelo;

import java.util.Arrays;

/**
 * Roles válidos del sistema, alineados con los códigos persistidos en la base.
 */
public enum RolUsuario {
    ANALISTA_SOC("ANALISTA_SOC"),
    SUPERVISOR_SEGURIDAD("SUPERVISOR_SEGURIDAD"),
    ADMINISTRADOR("ADMINISTRADOR");

    private final String codigo;

    RolUsuario(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static RolUsuario desdeCodigo(String codigo) {
        return Arrays.stream(values())
                .filter(rol -> rol.codigo.equals(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rol de usuario desconocido: " + codigo));
    }
}
