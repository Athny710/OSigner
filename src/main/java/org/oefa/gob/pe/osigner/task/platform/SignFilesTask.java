package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.application.RestService;
import org.oefa.gob.pe.osigner.application.SignService;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.CertificateModel;
import org.oefa.gob.pe.osigner.util.LogUtil;

import java.io.IOException;

public class SignFilesTask extends Task<Void> {


    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("[TASK] Firmando archivos", this.getClass().getName());

        SignService.signFiles(SignTaskManager.CERTIFICATE_MODEL);
        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN))
            RestService.updateSignProcess(Constant.FIRMA_ESTADO_FIRMADO);

        return null;

    }

    @Override
    protected void succeeded() {
        super.succeeded();
        NotificationFX.updateProgressNotification("Comprimiendo archivos firmados");
        Platform.runLater(() -> {
            StepComponent.showStepCompleted(2);
        });

    }


    @Override
    protected void failed() {
        super.failed();
        System.out.println(super.getException().getCause());

        String errorMessage = LogUtil.setError(
                "Error firmando los archivos",
                this.getClass().getName(),
                new Exception(super.getException().getMessage())
        );
        StepComponent.showStepError(2);
        NotificationFX.closeProgressNotification();
        if(super.getException().getCause() instanceof IOException){
            NotificationFX.showTimestampErrorNotification("Error firmando los archivos");
        }else{
            NotificationFX.showFatalErrorNotification(errorMessage);
        }

    }
}
