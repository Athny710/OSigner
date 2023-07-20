package org.oefa.gob.pe.osigner.task.configuration;

import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.application.RestService;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class SignProcessConfigurationTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        SignConfiguration.createInstace(
                RestService.getSignConfigurationModel()
        );
        return null;

    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("[CONFIGURACION] Se obtuvo la configuración del proceso de firma", this.getClass().getName());
        super.succeeded();
        ConfigurationTaskManager.setConfigurationFinished();

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error obteniendo obteniendo la configuración del proceso de firma",
                this.getClass().getName(),
                (Exception) super.getException()
        );

        NotificationFX.showCrlErrorNotification(errorMessage);

    }
}
