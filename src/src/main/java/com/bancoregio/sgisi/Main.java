package com.bancoregio.sgisi;

import com.bancoregio.sgisi.dao.impl.*;
import com.bancoregio.sgisi.presentacion.DialogLogin;
import com.bancoregio.sgisi.presentacion.UiStyles;
import com.bancoregio.sgisi.presentacion.VentanaPrincipal;
import com.bancoregio.sgisi.servicio.AuthService;
import com.bancoregio.sgisi.servicio.CatalogoService;
import com.bancoregio.sgisi.servicio.GestorSLA;
import com.bancoregio.sgisi.servicio.IncidenteService;

import javax.swing.*;

/**
 * Punto de entrada del prototipo SGISI.
 *
 * La clase arma manualmente las dependencias principales de la aplicación
 * siguiendo una arquitectura por capas: la interfaz Swing usa servicios, los
 * servicios usan DAOs y los DAOs acceden a la base de datos. Para este proyecto
 * no se utiliza un framework de inyección de dependencias; por eso las
 * implementaciones concretas se conectan aquí.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UiStyles.applyLookAndFeel();

            // Composición de objetos de la aplicación: se conectan servicios y DAOs
            // una sola vez antes de abrir las ventanas.
            AuthService authService = new AuthService(new UsuarioJDBC());
            CatalogoService catalogoService = new CatalogoService(new CatalogoJDBC());
            IncidenteService incidenteService = new IncidenteService(new IncidenteJDBC(), new BitacoraJDBC(), new GestorSLA(new SlaJDBC()));

            // El login es modal: el flujo queda detenido hasta que el usuario
            // se autentica o cierra la ventana.
            DialogLogin login = new DialogLogin(null, authService);
            login.setVisible(true);
            if (login.getUsuarioAutenticado() != null) {
                new VentanaPrincipal(incidenteService, catalogoService, login.getUsuarioAutenticado()).setVisible(true);
            }
        });
    }
}
