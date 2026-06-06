package com.bancoregio.sgisi.modelo;

/**
 * Clase base para los usuarios del sistema.
 *
 * Permite representar roles específicos mediante subclases, manteniendo campos
 * comunes como identidad, email y rol. En este prototipo las subclases no agregan
 * comportamiento propio, pero hacen explícita la clasificación de usuarios del
 * dominio.
 */
public abstract class Usuario {
    private final int id;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final String rol;

    public Usuario(int id, String nombre, String apellido, String email, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
