package com.bancoregio.sgisi.presentacion;

import com.bancoregio.sgisi.modelo.ActivoAfectado;
import com.bancoregio.sgisi.modelo.NivelSeveridad;
import com.bancoregio.sgisi.modelo.TipoIncidente;
import com.bancoregio.sgisi.modelo.Usuario;
import com.bancoregio.sgisi.servicio.CatalogoService;
import com.bancoregio.sgisi.servicio.IncidenteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Formulario UC02 registrar incidente.
 */
public class FormRegistroIncidente extends JDialog {
    /**
     * Crea formulario de registro.
     */
    public FormRegistroIncidente(Frame owner, CatalogoService catalogoService, IncidenteService incidenteService, Usuario usuario, Runnable onDone) {
        super(owner, "Registrar incidente", true);

        JPanel root = new JPanel(new BorderLayout(8, 10));
        root.setBorder(new EmptyBorder(14, 16, 12, 16));

        JLabel titulo = new JLabel("Nuevo incidente", SwingConstants.LEFT);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        root.add(titulo, BorderLayout.NORTH);

        JPanel form = UiStyles.createFormPanel();
        JComboBox<TipoIncidente> tipos = new JComboBox<>();
        JComboBox<NivelSeveridad> severidades = new JComboBox<>();
        JComboBox<ActivoAfectado> activos = new JComboBox<>();
        JTextArea desc = new JTextArea(5, 24);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(desc);
        UiStyles.addFormRow(form, 0, "Tipo:", tipos);
        UiStyles.addFormRow(form, 1, "Severidad:", severidades);
        UiStyles.addFormRow(form, 2, "Activo afectado:", activos);
        UiStyles.addFormRow(form, 3, "Descripción:", descScroll);
        root.add(form, BorderLayout.CENTER);

        JLabel ayuda = new JLabel("Completá todos los campos antes de guardar.");
        ayuda.setForeground(new Color(90, 90, 90));
        JButton guardar = new JButton("Guardar");
        JPanel acciones = new JPanel(new BorderLayout());
        acciones.add(ayuda, BorderLayout.WEST);
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightButtons.add(guardar);
        acciones.add(rightButtons, BorderLayout.EAST);
        root.add(acciones, BorderLayout.SOUTH);
        setContentPane(root);

        try {
            for (TipoIncidente t : catalogoService.listarTipos()) tipos.addItem(t);
            for (NivelSeveridad s : catalogoService.listarSeveridades()) severidades.addItem(s);
            for (ActivoAfectado a : catalogoService.listarActivos()) activos.addItem(a);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al cargar catálogos", JOptionPane.ERROR_MESSAGE);
        }

        guardar.addActionListener(e -> {
            try {
                incidenteService.registrarIncidente((TipoIncidente) tipos.getSelectedItem(), (NivelSeveridad) severidades.getSelectedItem(), (ActivoAfectado) activos.getSelectedItem(), desc.getText(), usuario);
                JOptionPane.showMessageDialog(this, "Incidente registrado correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                onDone.run();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar el incidente", JOptionPane.ERROR_MESSAGE);
            }
        });

        getRootPane().setDefaultButton(guardar);
        setMinimumSize(new Dimension(560, 430));
        pack();
        setLocationRelativeTo(owner);
    }
}
