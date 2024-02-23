package org.oefa.gob.pe.osigner.task.configuration;

import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.application.RestService;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class SignProcessConfigurationTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("[CONFIGURACION - FIRMA] Iniciando configuración de firma", this.getClass().getName());
        Thread.sleep(Constant.INITIALIZATION_APP_DELAY_TIME);
        RestService.getSignConfiguration();

        return null;

    }


    @Override
    protected void succeeded() {
        LogUtil.setInfo("[CONFIGURACION - FIRMA] Se obtuvo la configuración del proceso de firma", this.getClass().getName());
        super.succeeded();
        ConfigurationTaskManager.setConfigurationFinished();

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error obteniendo obteniendo la configuración del proceso de firma.",
                this.getClass().getName(),
                (Exception) super.getException()
        );

        NotificationFX.showFatalErrorNotification(errorMessage);

    }

}
