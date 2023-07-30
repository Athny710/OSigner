package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.application.ProgressService;
import org.oefa.gob.pe.osigner.application.RestService;
import org.oefa.gob.pe.osigner.application.SignService;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.ProgressComponent;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.FileUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class UploadFilesTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("Zipeando los archivos firmados.", this.getClass().getName());
        ProgressComponent.updateMessageAndProgress(
                "Comprimiendo archivos firmados",
                0.4
        );
        FileUtil.zipFiles(
                SignConfiguration.getInstance().getSignProcessConfiguration().getZipName(),
                SignConfiguration.getInstance().getFilesToSign()
        );

        LogUtil.setInfo("Subiendo los archivos firmados.", this.getClass().getName());
        ProgressComponent.updateMessageAndProgress(
                "Comprimiendo archivos firmados",
                0.8
        );
        RestService.uploadFilesSigned(
                SignConfiguration.getInstance()
        );
        ProgressComponent.updateProgress(1);
        ProgressService.closeProgressNotification();
        return null;

    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se subieron los archivos firmados exitosamente", this.getClass().getName());
        super.succeeded();

        Platform.runLater(() -> {
            StepComponent.showStepCompleted(3);
            PlatformService.showSuccessAndClose();
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
        StepComponent.showStepError(3);
        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
