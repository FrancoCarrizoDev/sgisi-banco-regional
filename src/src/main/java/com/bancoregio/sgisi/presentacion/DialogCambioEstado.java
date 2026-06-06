package com.bancoregio.sgisi.presentacion;

import com.bancoregio.sgisi.modelo.Usuario;
import com.bancoregio.sgisi.servicio.IncidenteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Diálogo modal del UC04 Cambiar estado.
 *
 * Muestra únicamente los estados destino permitidos por el estado actual del
 * incidente. De esta manera la interfaz acompaña la regla del dominio y reduce
 * la posibilidad de solicitar transiciones inválidas.
 */
public class DialogCambioEstado extends JDialog {
    public DialogCambioEstado(Frame owner, IncidenteService service, Usuario usuario, int incidenteId, Runnable onDone) {
        super(owner, "Cambiar estado", true);

        JPanel root = new JPanel(new BorderLayout(8, 10));
        root.setBorder(new EmptyBorder(14, 16, 12, 16));
        root.add(new JLabel("Incidente #" + incidenteId + " - Seleccioná el nuevo estado"), BorderLayout.NORTH);

        JPanel form = UiStyles.createFormPanel();
        JComboBox<String> destino = new JComboBox<>();
        JTextField obs = new JTextField();
        UiStyles.addFormRow(form, 0, "Estado destino:", destino);
        UiStyles.addFormRow(form, 1, "Observación:", obs);
        root.add(form, BorderLayout.CENTER);

        JButton ok = new JButton("Confirmar cambio");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.add(ok);
        root.add(actions, BorderLayout.SOUTH);
        setContentPane(root);

        try {
            // Las transiciones salen del patrón State. La UI no mantiene una
            // matriz propia de estados válidos.
            for (String t : service.obtenerPorId(incidenteId).getEstado().getTransicionesValidas()) destino.addItem(t);
            if (destino.getItemCount() == 0) {
                ok.setEnabled(false);
                JOptionPane.showMessageDialog(this, "El incidente no tiene transiciones válidas desde su estado actual.", "Sin transiciones", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ok.setEnabled(false);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar transiciones", JOptionPane.ERROR_MESSAGE);
        }

        ok.addActionListener(e -> {
            try {
                // El servicio vuelve a validar la transición antes de persistir,
                // aunque la UI ya haya limitado las opciones visibles.
                service.cambiarEstado(incidenteId, String.valueOf(destino.getSelectedItem()), obs.getText().trim(), usuario);
                JOptionPane.showMessageDialog(this, "Estado actualizado correctamente.", "Cambio aplicado", JOptionPane.INFORMATION_MESSAGE);
                onDone.run();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo cambiar el estado", JOptionPane.ERROR_MESSAGE);
            }
        });

        getRootPane().setDefaultButton(ok);
        setMinimumSize(new Dimension(500, 270));
        pack();
        setLocationRelativeTo(owner);
    }
}
