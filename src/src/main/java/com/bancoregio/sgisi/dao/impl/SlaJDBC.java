package com.bancoregio.sgisi.dao.impl;

import com.bancoregio.sgisi.dao.SlaDAO;
import com.bancoregio.sgisi.modelo.ConfiguracionSLA;
import com.bancoregio.sgisi.util.ConexionDB;
import java.sql.SQLException;
import java.util.Optional;

/** Implementación JDBC de SLA. */
public class SlaJDBC implements SlaDAO {
    /** {@inheritDoc} */
    public Optional<ConfiguracionSLA> buscarPorSeveridad(int severidadId) throws SQLException {
        try(var c=ConexionDB.getInstance().getConnection(); var ps=c.prepareStatement("SELECT id,severidad_id,plazo_horas,descripcion FROM configuracion_sla WHERE severidad_id=?")){
            ps.setInt(1,severidadId);
            try(var rs=ps.executeQuery()){ if(!rs.next()) return Optional.empty(); return Optional.of(new ConfiguracionSLA(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getString(4))); }
        }
    }
}
