package com.bancoregio.sgisi.presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Utilidades compartidas de la interfaz Swing.
 *
 * Evita repetir configuración visual en cada ventana y concentra decisiones de
 * estilo simples, como Look & Feel, márgenes y estructura de formularios.
 */
public final class UiStyles {
    private UiStyles() {
    }

    /**
     * Aplica un Look & Feel estándar priorizando Nimbus para que la aplicación se
     * vea más uniforme entre sistemas operativos.
     */
    public static void applyLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Si falla el L&F, Swing usa el default.
        }
    }

    /**
     * Crea un panel preparado para formularios de dos columnas: etiqueta y campo.
     * GridBagLayout permite alinear filas sin fijar tamaños absolutos.
     */
    public static JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        return panel;
    }

    /**
     * Crea un borde interno uniforme para contenedores principales.
     */
    public static EmptyBorder contentPadding() {
        return new EmptyBorder(12, 12, 12, 12);
    }

    /**
     * Encapsula la configuración repetitiva de GridBagConstraints para que los
     * formularios mantengan la misma separación y alineación.
     */
    public static void addFormRow(JPanel panel, int row, String label, JComponent field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 4, 6, 10);
        panel.add(new JLabel(label), gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);
        panel.add(field, gbc);
    }
}
