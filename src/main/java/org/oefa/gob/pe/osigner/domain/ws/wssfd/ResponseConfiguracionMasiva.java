package org.oefa.gob.pe.osigner.domain.ws.wssfd;

import org.oefa.gob.pe.osigner.domain.ws.MassiveSignatureResponse;

public class ResponseConfiguracionMasiva {

    private boolean status;
    private String mensaje;
    private MassiveSignatureResponse responseFirmaMasiva;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public MassiveSignatureResponse getResponseFirmaMasiva() {
        return responseFirmaMasiva;
    }

    public void setResponseFirmaMasiva(MassiveSignatureResponse responseFirmaMasiva) {
        this.responseFirmaMasiva = responseFirmaMasiva;
    }
}
