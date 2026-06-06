package com.bancoregio.sgisi.modelo;

import com.bancoregio.sgisi.modelo.estado.EstadoIncidenteState;

import java.time.LocalDateTime;

/**
 * Entidad central del dominio SGISI.
 *
 * Representa un incidente de seguridad con su clasificación, activo afectado,
 * fechas de seguimiento y estado actual. El estado se modela como objeto
 * EstadoIncidenteState para aplicar el patrón State en lugar de manejar el ciclo
 * de vida con condicionales dispersos.
 */
public class Incidente {
    private Integer id;
    private TipoIncidente tipo;
    private NivelSeveridad severidad;
    private ActivoAfectado activo;
    private String descripcion;
    private LocalDateTime fechaDeteccion;
    private LocalDateTime fechaVencimientoSla;
    private LocalDateTime fechaCierre;
    private EstadoIncidenteState estado;

    public Incidente(Integer id, TipoIncidente tipo, NivelSeveridad severidad, ActivoAfectado activo, String descripcion, LocalDateTime fechaDeteccion, LocalDateTime fechaVencimientoSla, LocalDateTime fechaCierre, EstadoIncidenteState estado) {
        this.id = id;
        this.tipo = tipo;
        this.severidad = severidad;
        this.activo = activo;
        this.descripcion = descripcion;
        this.fechaDeteccion = fechaDeteccion;
        this.fechaVencimientoSla = fechaVencimientoSla;
        this.fechaCierre = fechaCierre;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoIncidente getTipo() {
        return tipo;
    }

    public NivelSeveridad getSeveridad() {
        return severidad;
    }

    public ActivoAfectado getActivo() {
        return activo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaDeteccion() {
        return fechaDeteccion;
    }

    public LocalDateTime getFechaVencimientoSla() {
        return fechaVencimientoSla;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public EstadoIncidenteState getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidenteState estado) {
        this.estado = estado;
    }
}
