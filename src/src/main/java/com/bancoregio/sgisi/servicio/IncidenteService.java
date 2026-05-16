package com.bancoregio.sgisi.servicio;

import com.bancoregio.sgisi.dao.BitacoraDAO;
import com.bancoregio.sgisi.dao.IncidenteDAO;
import com.bancoregio.sgisi.dao.impl.IncidenteJDBC;
import com.bancoregio.sgisi.modelo.*;
import com.bancoregio.sgisi.modelo.estado.EstadoDetectado;
import com.bancoregio.sgisi.modelo.estado.EstadoFactory;
import com.bancoregio.sgisi.util.ConexionDB;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio principal para UC02, UC04 y UC08.
 */
public class IncidenteService {
    private final IncidenteDAO incidenteDAO;
    private final BitacoraDAO bitacoraDAO;
    private final GestorSLA gestorSLA;

    /**
     * Crea servicio de incidentes.
     */
    public IncidenteService(IncidenteDAO incidenteDAO, BitacoraDAO bitacoraDAO, GestorSLA gestorSLA) {
        this.incidenteDAO = incidenteDAO;
        this.bitacoraDAO = bitacoraDAO;
        this.gestorSLA = gestorSLA;
    }

    /**
     * Registra incidente y bitácora en una sola transacción.
     */
    public Incidente registrarIncidente(TipoIncidente tipo, NivelSeveridad sev, ActivoAfectado act, String descripcion, Usuario usuario) throws SQLException {
        LocalDateTime deteccion = LocalDateTime.now();
        LocalDateTime sla = gestorSLA.calcularVencimiento(sev.id(), deteccion);
        Incidente i = new Incidente(null, tipo, sev, act, descripcion, deteccion, sla, null, new EstadoDetectado());
        try (var c = ConexionDB.getInstance().getConnection()) {
            c.setAutoCommit(false);
            try {
                int id = incidenteDAO.insertar(i, c);
                i.setId(id);
                bitacoraDAO.insertar(new EntradaBitacora(null, id, usuario.getId(), LocalDateTime.now(), "REGISTRO", "Incidente registrado"), c);
                c.commit();
                return i;
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    /**
     * Cambia estado validando transición desde state actual.
     */
    public Incidente cambiarEstado(int incidenteId, String destino, String observacion, Usuario usuario) throws SQLException {
        Incidente i = incidenteDAO.buscarPorId(incidenteId).orElseThrow(() -> new IllegalArgumentException("Incidente no existe"));
        if (!i.getEstado().puedeTransicionarA(destino)) throw new IllegalStateException("Transición inválida");
        try (var c = ConexionDB.getInstance().getConnection()) {
            c.setAutoCommit(false);
            try {
                int estadoId = ((IncidenteJDBC) incidenteDAO).estadoIdPorNombre(destino, c);
                LocalDateTime cierre = "CERRADO".equals(destino) ? LocalDateTime.now() : null;
                incidenteDAO.actualizarEstado(incidenteId, estadoId, cierre, c);
                bitacoraDAO.insertar(new EntradaBitacora(null, incidenteId, usuario.getId(), LocalDateTime.now(), "CAMBIO_ESTADO", observacion), c);
                c.commit();
                i.setEstado(EstadoFactory.crear(destino));
                i.setFechaCierre(cierre);
                return i;
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    /**
     * Lista incidentes con filtros opcionales.
     */
    public List<Incidente> listarIncidentes(Integer estadoId, Integer severidadId) throws SQLException {
        return incidenteDAO.listar(estadoId, severidadId);
    }

    /**
     * Obtiene incidente por id.
     */
    public Incidente obtenerPorId(int id) throws SQLException {
        return incidenteDAO.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Incidente no existe"));
    }
}
