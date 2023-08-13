package org.oefa.gob.pe.osigner.domain.ws.wssfd;

import org.oefa.gob.pe.osigner.domain.ws.ArchivoFirmado;

import java.util.List;

public class FirmaFinalizadaRequest {

    private List<ArchivoFirmado> archivos;


    public FirmaFinalizadaRequest() {}

    public FirmaFinalizadaRequest(List<ArchivoFirmado> archivos) {
        this.archivos = archivos;
    }

    public List<ArchivoFirmado> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ArchivoFirmado> archivos) {
        this.archivos = archivos;
    }
}
