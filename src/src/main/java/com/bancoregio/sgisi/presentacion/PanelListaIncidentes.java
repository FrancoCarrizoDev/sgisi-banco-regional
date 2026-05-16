package com.bancoregio.sgisi.presentacion;

import com.bancoregio.sgisi.modelo.Incidente;
import com.bancoregio.sgisi.modelo.NivelSeveridad;
import com.bancoregio.sgisi.modelo.Usuario;
import com.bancoregio.sgisi.servicio.CatalogoService;
import com.bancoregio.sgisi.servicio.IncidenteService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de UC08 listado y accesos a UC02/UC04.
 */
public class PanelListaIncidentes extends JPanel {
    private final IncidenteService incidenteService;
    private final CatalogoService catalogoService;
    private final Usuario usuario;
    private final JComboBox<Object> cbEstado = new JComboBox<>();
    private final JComboBox<Object> cbSeveridad = new JComboBox<>();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Tipo", "Severidad", "Estado", "Activo", "SLA"}, 0);
    private final JTable tabla = new JTable(model);

    /**
     * Crea panel de listado.
     */
    public PanelListaIncidentes(IncidenteService incidenteService, CatalogoService catalogoService, Usuario usuario) {
        this.incidenteService = incidenteService;
        this.catalogoService = catalogoService;
        this.usuario = usuario;
        setLayout(new BorderLayout(8, 8));
        setBorder(UiStyles.contentPadding());

        JPanel top = new JPanel(new BorderLayout(8, 8));
        JLabel titulo = new JLabel("Incidentes de Seguridad");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        top.add(titulo, BorderLayout.NORTH);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barra.setBorder(new TitledBorder("Filtros y acciones"));
        JButton filtrar = new JButton("Filtrar");
        JButton nuevo = new JButton("Registrar incidente");
        JButton estado = new JButton("Cambiar estado");
        barra.add(new JLabel("Estado:"));
        barra.add(cbEstado);
        barra.add(new JLabel("Severidad:"));
        barra.add(cbSeveridad);
        barra.add(filtrar);
        barra.add(nuevo);
        barra.add(estado);
        top.add(barra, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        tabla.setRowHeight(24);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(170);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(190);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        filtrar.addActionListener(e -> cargarTabla());
        nuevo.addActionListener(e -> new FormRegistroIncidente((Frame) SwingUtilities.getWindowAncestor(this), catalogoService, incidenteService, usuario, this::cargarTabla).setVisible(true));
        estado.addActionListener(e -> abrirCambioEstado());
        cargarCombos();
        cargarTabla();
    }

    private void cargarCombos() {
        try {
            cbEstado.addItem("TODOS");
            cbEstado.addItem("1-DETECTADO");
            cbEstado.addItem("2-EN_ANALISIS");
            cbEstado.addItem("3-CONTENIDO");
            cbEstado.addItem("4-EN_REMEDIACION");
            cbEstado.addItem("5-CERRADO");
            cbEstado.addItem("6-FALSO_POSITIVO");
            cbSeveridad.addItem("TODAS");
            for (NivelSeveridad s : catalogoService.listarSeveridades()) cbSeveridad.addItem(s.id() + "-" + s.nombre());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al cargar filtros", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer selectedId(JComboBox<Object> cb) {
        String v = String.valueOf(cb.getSelectedItem());
        return v.contains("-") ? Integer.parseInt(v.split("-")[0]) : null;
    }

    private void cargarTabla() {
        try {
            List<Incidente> list = incidenteService.listarIncidentes(selectedId(cbEstado), selectedId(cbSeveridad));
            model.setRowCount(0);
            for (Incidente i : list)
                model.addRow(new Object[]{i.getId(), i.getTipo().nombre(), i.getSeveridad().nombre(), i.getEstado().getNombre(), i.getActivo().nombre(), i.getFechaVencimientoSla()});
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al listar incidentes", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirCambioEstado() {
        int selectedRow = tabla.getSelectedRow();
        Integer incidenteId = null;
        if (selectedRow >= 0) {
            Object value = tabla.getValueAt(selectedRow, 0);
            if (value instanceof Integer) {
                incidenteId = (Integer) value;
            } else if (value != null) {
                incidenteId = Integer.parseInt(String.valueOf(value));
            }
        }
        if (incidenteId == null) {
            String id = JOptionPane.showInputDialog(this, "Ingresá el ID del incidente a actualizar:");
            if (id == null || id.isBlank()) return;
            try {
                incidenteId = Integer.parseInt(id.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser numérico.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        new DialogCambioEstado((Frame) SwingUtilities.getWindowAncestor(this), incidenteService, usuario, incidenteId, this::cargarTabla).setVisible(true);
    }
}
