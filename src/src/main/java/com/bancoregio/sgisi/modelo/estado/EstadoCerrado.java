package com.bancoregio.sgisi.modelo.estado;
import java.util.Set;
/** Estado cerrado. */
public class EstadoCerrado extends EstadoIncidenteState { public String getNombre(){return "CERRADO";} public Set<String> getTransicionesValidas(){ return Set.of(); } }
