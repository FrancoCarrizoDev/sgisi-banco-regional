package com.bancoregio.sgisi.presentacion;

import com.bancoregio.sgisi.modelo.Usuario;
import com.bancoregio.sgisi.servicio.CatalogoService;
import com.bancoregio.sgisi.servicio.IncidenteService;
import javax.swing.*;
import java.awt.*;

/** Ventana principal de SGISI. */
public class VentanaPrincipal extends JFrame {
    /** Crea ventana principal. */
    public VentanaPrincipal(IncidenteService incidenteService, CatalogoService catalogoService, Usuario usuario) {
        super("SGISI - " + usuario.getNombreCompleto());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1180, 680);
        setMinimumSize(new Dimension(1024, 620));
        setContentPane(new PanelListaIncidentes(incidenteService,catalogoService,usuario));
        setLocationRelativeTo(null);
    }
}
