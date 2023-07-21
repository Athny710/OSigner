package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.application.RestService;
import org.oefa.gob.pe.osigner.application.SignService;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class UploadFilesTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        RestService.uploadFilesSigned(
                SignConfiguration.getInstance()
        );
        return null;

    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se firmaron los archivos exitosamente", this.getClass().getName());
        super.succeeded();

        Platform.runLater(() -> {
            StepComponent.showStepCompleted(1);

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
        StepComponent.showStepError(1);
        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
