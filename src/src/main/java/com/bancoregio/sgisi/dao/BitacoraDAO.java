package com.bancoregio.sgisi.dao;

import com.bancoregio.sgisi.modelo.EntradaBitacora;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Contrato de acceso a datos para auditoría.
 *
 * La bitácora registra acciones relevantes sobre incidentes. Recibe la conexión
 * desde el servicio para que el registro de auditoría forme parte de la misma
 * transacción que la operación principal.
 */
public interface BitacoraDAO {
    void insertar(EntradaBitacora entrada, Connection connection) throws SQLException;
}
