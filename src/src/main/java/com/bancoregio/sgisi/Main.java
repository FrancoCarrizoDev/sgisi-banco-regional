package com.bancoregio.sgisi;

import com.bancoregio.sgisi.dao.impl.*;
import com.bancoregio.sgisi.presentacion.DialogLogin;
import com.bancoregio.sgisi.presentacion.UiStyles;
import com.bancoregio.sgisi.presentacion.VentanaPrincipal;
import com.bancoregio.sgisi.servicio.*;
import javax.swing.*;

/** Punto de entrada del prototipo SGISI. */
public class Main {
    /** Arranca la app en flujo UC01 login. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UiStyles.applyLookAndFeel();
            AuthService authService=new AuthService(new UsuarioJDBC());
            CatalogoService catalogoService=new CatalogoService(new CatalogoJDBC());
            IncidenteService incidenteService=new IncidenteService(new IncidenteJDBC(), new BitacoraJDBC(), new GestorSLA(new SlaJDBC()));
            DialogLogin login=new DialogLogin(null,authService); login.setVisible(true);
            if(login.getUsuarioAutenticado()!=null){ new VentanaPrincipal(incidenteService,catalogoService,login.getUsuarioAutenticado()).setVisible(true); }
        });
    }
}
