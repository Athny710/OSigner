package org.oefa.gob.pe.osigner.domain.ws.rest;

import org.oefa.gob.pe.osigner.domain.ws.ArchivoFirmado;

import java.util.List;

public class FirmaMasivaFinalizadaRequest {

    private Integer grupoOperacion;
    private String clientUUID;
    private String optional;
    private List<ArchivoFirmado> archivos;

    public Integer getGrupoOperacion() {
        return grupoOperacion;
    }

    public void setGrupoOperacion(Integer grupoOperacion) {
        this.grupoOperacion = grupoOperacion;
    }

    public String getClientUUID() {
        return clientUUID;
    }

    public void setClientUUID(String clientUUID) {
        this.clientUUID = clientUUID;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public List<ArchivoFirmado> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ArchivoFirmado> archivos) {
        this.archivos = archivos;
    }
}
