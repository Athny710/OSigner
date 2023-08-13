package org.oefa.gob.pe.osigner.task.configuration;

import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.util.CertificateUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class CertificateConfigurationTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        Thread.sleep(Constant.INITIALIZATION_APP_DELAY_TIME);
        CertificateUtil.loadCertificates();

        return null;

    }


    @Override
    protected void succeeded() {
        LogUtil.setInfo("[CONFIGURACION] Se cargaron los certificados del ordenador", this.getClass().getName());
        super.succeeded();
        ConfigurationTaskManager.setConfigurationFinished();

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error obteniendo/validando los certificados.",
                this.getClass().getName(),
                (Exception) super.getException()
        );

        NotificationFX.showCrlErrorNotification(errorMessage);

    }

}
