package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.application.RestService;
import org.oefa.gob.pe.osigner.application.SignService;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.util.CertificateUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class CertificateTask extends Task<Void> {

    private final String certificateAlias;
    public CertificateTask(String certificateAlias){
        this.certificateAlias = certificateAlias;
    }

    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("[TASK] Obteniendo información del certificado", this.getClass().getName());

        SignTaskManager.CERTIFICATE_MODEL = CertificateUtil.getCertificateToSignByAlias(certificateAlias);

        return null;

    }

    @Override
    protected void succeeded() {
        super.succeeded();
        Platform.runLater(() -> {
            StepComponent.showStepCompleted(1);
            PlatformService.disableButtons(true);
        });

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error obteneniendo información del certificado",
                this.getClass().getName(),
                (Exception) super.getException()
        );
        NotificationFX.showFatalErrorNotification(errorMessage);
        PlatformService.disableButtons(false);
    }
}
