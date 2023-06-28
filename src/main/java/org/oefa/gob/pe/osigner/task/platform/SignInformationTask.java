package org.oefa.gob.pe.osigner.task.platform;

import io.github.palexdev.materialfx.enums.NotificationPos;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.CertificateComponent;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.util.CertificateUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class SignInformationTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("Obteniendo información de firma", this.getClass().getName());
        Thread.sleep(3000);
        return null;
    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se obtuvo información del proceso de firma", this.getClass().getName());
        super.succeeded();

        Platform.runLater(new Runnable() {
            @Override public void run() {
                //CertificateComponent.loadCertificates(CertificateUtil.getUserCertificateList(AppConfiguration.DNI_CLIENT));
                StepComponent.showStepCompleted(0);
                PlatformService.signInformationDownloaded();

            }
        });

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
        "Error obteniendo el proceso de firma",
                this.getClass().getName(),
                (Exception) super.getException()
        );

        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
