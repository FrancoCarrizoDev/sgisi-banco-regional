package com.bancoregio.sgisi.dao;

import com.bancoregio.sgisi.modelo.Usuario;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Contrato de acceso a datos para usuarios.
 *
 * Expone solo la operación necesaria para autenticar: buscar un usuario activo
 * por email y hash de contraseña.
 */
public interface UsuarioDAO {
    Optional<Usuario> buscarActivoPorEmailYHash(String email, String hash) throws SQLException;
}
