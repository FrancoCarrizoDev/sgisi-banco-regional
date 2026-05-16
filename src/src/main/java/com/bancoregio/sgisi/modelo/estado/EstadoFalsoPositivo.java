package com.bancoregio.sgisi.modelo.estado;
import java.util.Set;
/** Estado falso positivo. */
public class EstadoFalsoPositivo extends EstadoIncidenteState { public String getNombre(){return "FALSO_POSITIVO";} public Set<String> getTransicionesValidas(){ return Set.of(); } }
