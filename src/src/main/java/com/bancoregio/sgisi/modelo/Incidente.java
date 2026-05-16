package com.bancoregio.sgisi.modelo;

import com.bancoregio.sgisi.modelo.estado.EstadoIncidenteState;
import java.time.LocalDateTime;

/** Entidad incidente del SGISI. */
public class Incidente {
    private Integer id; private TipoIncidente tipo; private NivelSeveridad severidad; private ActivoAfectado activo;
    private String descripcion; private LocalDateTime fechaDeteccion; private LocalDateTime fechaVencimientoSla; private LocalDateTime fechaCierre;
    private EstadoIncidenteState estado;
    /** Crea un incidente. */
    public Incidente(Integer id, TipoIncidente tipo, NivelSeveridad severidad, ActivoAfectado activo, String descripcion, LocalDateTime fechaDeteccion, LocalDateTime fechaVencimientoSla, LocalDateTime fechaCierre, EstadoIncidenteState estado) {
        this.id=id; this.tipo=tipo; this.severidad=severidad; this.activo=activo; this.descripcion=descripcion; this.fechaDeteccion=fechaDeteccion; this.fechaVencimientoSla=fechaVencimientoSla; this.fechaCierre=fechaCierre; this.estado=estado;
    }
    /** Devuelve id. */ public Integer getId(){return id;} /** Define id. */ public void setId(Integer id){this.id=id;}
    /** Devuelve tipo. */ public TipoIncidente getTipo(){return tipo;} /** Devuelve severidad. */ public NivelSeveridad getSeveridad(){return severidad;}
    /** Devuelve activo. */ public ActivoAfectado getActivo(){return activo;} /** Devuelve descripcion. */ public String getDescripcion(){return descripcion;}
    /** Devuelve fecha detección. */ public LocalDateTime getFechaDeteccion(){return fechaDeteccion;} /** Devuelve SLA. */ public LocalDateTime getFechaVencimientoSla(){return fechaVencimientoSla;}
    /** Devuelve cierre. */ public LocalDateTime getFechaCierre(){return fechaCierre;} /** Define cierre. */ public void setFechaCierre(LocalDateTime fechaCierre){this.fechaCierre=fechaCierre;}
    /** Devuelve estado actual. */ public EstadoIncidenteState getEstado(){return estado;} /** Define estado actual. */ public void setEstado(EstadoIncidenteState estado){this.estado=estado;}
}
