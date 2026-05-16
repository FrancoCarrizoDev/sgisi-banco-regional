package com.bancoregio.sgisi.presentacion;

import com.bancoregio.sgisi.modelo.Usuario;
import com.bancoregio.sgisi.servicio.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Diálogo de login UC01.
 */
public class DialogLogin extends JDialog {
    private Usuario usuarioAutenticado;

    /**
     * Crea diálogo de autenticación.
     */
    public DialogLogin(Frame owner, AuthService authService) {
        super(owner, "Ingreso SGISI", true);

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(new EmptyBorder(16, 18, 14, 18));

        JLabel titulo = new JLabel("Sistema de Gestión de Incidentes", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 15f));
        root.add(titulo, BorderLayout.NORTH);

        JPanel form = UiStyles.createFormPanel();
        JTextField email = new JTextField(24);
        JPasswordField pass = new JPasswordField(24);
        UiStyles.addFormRow(form, 0, "Email:", email);
        UiStyles.addFormRow(form, 1, "Contraseña:", pass);
        root.add(form, BorderLayout.CENTER);

        JButton btn = new JButton("Ingresar");
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        acciones.add(btn);
        root.add(acciones, BorderLayout.SOUTH);

        setContentPane(root);

        btn.addActionListener(e -> {
            try {
                String correo = email.getText().trim();
                String clave = new String(pass.getPassword());
                usuarioAutenticado = authService.autenticar(correo, clave);
                if (usuarioAutenticado != null) {
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales inválidas. Verificá email y contraseña.", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al iniciar sesión", JOptionPane.ERROR_MESSAGE);
            }
        });

        getRootPane().setDefaultButton(btn);
        setResizable(false);
        pack();
        setMinimumSize(new Dimension(420, getPreferredSize().height));
        setLocationRelativeTo(owner);
    }

    /**
     * Devuelve usuario autenticado o null.
     */
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
}
