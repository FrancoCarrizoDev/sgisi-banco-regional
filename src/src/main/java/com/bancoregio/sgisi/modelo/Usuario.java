package com.bancoregio.sgisi.modelo;

/**
 * Usuario base del sistema SGISI.
 */
public abstract class Usuario {
    private final int id;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final String rol;

    /**
     * Construye un usuario base.
     */
    public Usuario(int id, String nombre, String apellido, String email, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
    }

    /**
     * Devuelve el id.
     */
    public int getId() {
        return id;
    }

    /**
     * Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve apellido.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Devuelve email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Devuelve rol.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Devuelve nombre completo.
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
