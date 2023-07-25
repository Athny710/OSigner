package org.oefa.gob.pe.osigner.domain.ws.wssfd;

public class ResponseProcesoFirma {

    private boolean status;
    private String message;
    private GrupoProcesoFirma gpf;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GrupoProcesoFirma getGpf() {
        return gpf;
    }

    public void setGpf(GrupoProcesoFirma gpf) {
        this.gpf = gpf;
    }
}
