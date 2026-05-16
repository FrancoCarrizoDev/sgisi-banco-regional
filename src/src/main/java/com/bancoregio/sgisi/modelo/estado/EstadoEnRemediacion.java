package com.bancoregio.sgisi.modelo.estado;
import java.util.Set;
/** Estado en remediación. */
public class EstadoEnRemediacion extends EstadoIncidenteState { public String getNombre(){return "EN_REMEDIACION";} public Set<String> getTransicionesValidas(){ return Set.of("CERRADO","FALSO_POSITIVO"); } }
