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

public class SignFilesTask extends Task<Void> {

    private final CertificateModel certificateModel;

    public SignFilesTask(CertificateModel certificateModel){
        this.certificateModel = certificateModel;
    }

    @Override
    protected Void call() throws Exception {
        SignService.signFiles(this.certificateModel);
        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN))
            RestService.updateSignProcess(Constant.FIRMA_ESTADO_FIRMADO);

        return null;

    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se firmaron los archivos exitosamente", this.getClass().getName());
        super.succeeded();
        NotificationFX.updateProgressNotification("Comprimiendo archivos firmados");
        Platform.runLater(() -> {
            StepComponent.showStepCompleted(2);
        });

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error firmando los archivos",
                this.getClass().getName(),
                (Exception) super.getException()
        );
        StepComponent.showStepError(2);
        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
