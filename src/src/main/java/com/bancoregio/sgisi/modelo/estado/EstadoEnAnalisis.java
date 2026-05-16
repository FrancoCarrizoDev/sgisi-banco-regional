package com.bancoregio.sgisi.modelo.estado;
import java.util.Set;
/** Estado en análisis. */
public class EstadoEnAnalisis extends EstadoIncidenteState { public String getNombre(){return "EN_ANALISIS";} public Set<String> getTransicionesValidas(){ return Set.of("CONTENIDO","FALSO_POSITIVO"); } }
