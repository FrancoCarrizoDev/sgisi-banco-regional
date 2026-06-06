package com.bancoregio.sgisi.servicio;

import com.bancoregio.sgisi.dao.UsuarioDAO;
import com.bancoregio.sgisi.modelo.Usuario;
import com.bancoregio.sgisi.util.HashUtil;

import java.sql.SQLException;

/**
 * Servicio de autenticación del UC01.
 *
 * Centraliza la regla de login para que la pantalla no tenga que conocer cómo
 * se valida la contraseña ni cómo se consulta la base de datos. La UI entrega
 * email y contraseña en texto plano; el servicio transforma la contraseña a hash
 * y delega la búsqueda del usuario activo al DAO.
 */
public class AuthService {
    private final UsuarioDAO usuarioDAO;

    public AuthService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Autentica al usuario sin exponer contraseñas planas a la capa DAO.
     * Si no hay coincidencia, devuelve null para que la pantalla muestre el
     * mensaje de acceso denegado.
     */
    public Usuario autenticar(String email, String plainPassword) throws SQLException {
        return usuarioDAO.buscarActivoPorEmailYHash(email, HashUtil.sha256(plainPassword)).orElse(null);
    }
}
