package com.bancoregio.sgisi.servicio;

import com.bancoregio.sgisi.dao.UsuarioDAO;
import com.bancoregio.sgisi.modelo.Usuario;
import com.bancoregio.sgisi.util.HashUtil;

import java.sql.SQLException;

/**
 * Servicio de autenticación UC01.
 */
public class AuthService {
    private final UsuarioDAO usuarioDAO;

    /**
     * Crea servicio de autenticación.
     */
    public AuthService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Autentica usuario por email y password plano.
     */
    public Usuario autenticar(String email, String plainPassword) throws SQLException {
        return usuarioDAO.buscarActivoPorEmailYHash(email, HashUtil.sha256(plainPassword)).orElse(null);
    }
}
