package org.oefa.gob.pe.osigner.domain.ws;

import org.oefa.gob.pe.osigner.domain.SignConfigurationModel;

public class ResponseProcesoFirma {

    private boolean status;
    private String message;
    private SignConfigurationModel signConfigurationModel;

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

    public SignConfigurationModel getSignConfigurationModel() {
        return signConfigurationModel;
    }

    public void setSignConfigurationModel(SignConfigurationModel signConfigurationModel) {
        this.signConfigurationModel = signConfigurationModel;
    }
}
