package com.bancoregio.sgisi.dao;

import com.bancoregio.sgisi.modelo.Usuario;
import java.sql.SQLException;
import java.util.Optional;

/** DAO de usuarios. */
public interface UsuarioDAO {
    /** Busca usuario activo por email y hash. */
    Optional<Usuario> buscarActivoPorEmailYHash(String email, String hash) throws SQLException;
}
