package com.bancoregio.sgisi.dao;

import com.bancoregio.sgisi.modelo.Incidente;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos para incidentes.
 * <p>
 * El DAO separa la lógica de negocio del detalle de persistencia. Los servicios
 * trabajan contra esta interfaz y no contra SQL directo, lo que hace más clara la
 * división entre reglas del sistema y almacenamiento.
 */
public interface IncidenteDAO {
    /**
     * Inserta dentro de una conexión recibida desde el servicio para participar
     * de la misma transacción que la bitácora.
     */
    int insertar(Incidente incidente, Connection connection) throws SQLException;

    /**
     * Actualiza el estado persistido y, si corresponde, la fecha de cierre.
     */
    void actualizarEstado(int incidenteId, int estadoId, LocalDateTime fechaCierre, Connection connection) throws SQLException;

    /**
     * Reconstruye un incidente completo desde la base para que la capa de negocio
     * pueda validar reglas sobre el estado actual.
     */
    Optional<Incidente> buscarPorId(int id) throws SQLException;

    /**
     * Lista incidentes con filtros opcionales usados por la pantalla principal.
     */
    List<Incidente> listar(Integer estadoId, Integer severidadId) throws SQLException;

    /**
     * Traduce el nombre de estado del dominio al id de la clave foránea, dentro de
     * la conexión transaccional recibida. Forma parte del contrato para que la capa
     * de negocio dependa solo de la abstracción y no de la implementación concreta.
     */
    int estadoIdPorNombre(String nombre, Connection connection) throws SQLException;
}
