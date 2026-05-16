package com.bancoregio.sgisi.dao;

import com.bancoregio.sgisi.modelo.EntradaBitacora;
import java.sql.Connection;
import java.sql.SQLException;

/** DAO de bitácora. */
public interface BitacoraDAO { /** Inserta entrada de bitácora. */ void insertar(EntradaBitacora entrada, Connection connection) throws SQLException; }
