package com.bancoregio.sgisi.dao;

import com.bancoregio.sgisi.modelo.Incidente;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DAO de incidentes.
 */
public interface IncidenteDAO {
    /**
     * Inserta incidente en transacción activa.
     */
    int insertar(Incidente incidente, Connection connection) throws SQLException;

    /**
     * Actualiza estado de incidente.
     */
    void actualizarEstado(int incidenteId, int estadoId, LocalDateTime fechaCierre, Connection connection) throws SQLException;

    /**
     * Carga incidente por id.
     */
    Optional<Incidente> buscarPorId(int id) throws SQLException;

    /**
     * Lista incidentes filtrando opcionalmente.
     */
    List<Incidente> listar(Integer estadoId, Integer severidadId) throws SQLException;
}
