package com.bancoregio.sgisi.dao.impl;

import com.bancoregio.sgisi.dao.UsuarioDAO;
import com.bancoregio.sgisi.modelo.Administrador;
import com.bancoregio.sgisi.modelo.AnalistaSoc;
import com.bancoregio.sgisi.modelo.SupervisorSeguridad;
import com.bancoregio.sgisi.modelo.Usuario;
import com.bancoregio.sgisi.util.ConexionDB;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Implementación JDBC del DAO de usuarios.
 *
 * Además de consultar credenciales, reconstruye la subclase de Usuario según el
 * rol persistido. Así el resto del sistema trabaja con objetos del dominio y no
 * con filas de la tabla usuario.
 */
public class UsuarioJDBC implements UsuarioDAO {
    public Optional<Usuario> buscarActivoPorEmailYHash(String email, String hash) throws SQLException {
        String sql = "SELECT id,nombre,apellido,email,rol FROM usuario WHERE email=? AND contrasena_hash=? AND activo=1";
        try (var c = ConexionDB.getInstance().getConnection(); var ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, hash);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                int id = rs.getInt("id");
                String n = rs.getString("nombre");
                String a = rs.getString("apellido");
                String e = rs.getString("email");
                String r = rs.getString("rol");
                // El rol define qué especialización de Usuario se crea en memoria.
                return Optional.of(switch (r) {
                    case "ANALISTA_SOC" -> new AnalistaSoc(id, n, a, e);
                    case "SUPERVISOR_SEGURIDAD" -> new SupervisorSeguridad(id, n, a, e);
                    default -> new Administrador(id, n, a, e);
                });
            }
        }
    }
}
