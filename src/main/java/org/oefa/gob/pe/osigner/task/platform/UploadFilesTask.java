package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.application.RestService;
import org.oefa.gob.pe.osigner.core.AppFX;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.FileUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

import java.io.File;

public class UploadFilesTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("[TASK] Subiendo archivos firmados", this.getClass().getName());

        RestService.uploadFilesSigned(
                SignConfiguration.getInstance()
        );
        FileUtil.deleteFile(FileUtil.getSignedFolder() + SignConfiguration.getInstance().getSignProcessConfiguration().getZipUUID() + ".zip");

        return null;

    }

    @Override
    protected void succeeded() {
        super.succeeded();
        NotificationFX.closeProgressNotification();
        Platform.runLater(() -> {
            StepComponent.showStepCompleted(3);
            AppFX.showMessageAndCloseApplication(true);
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
        NotificationFX.showFatalErrorNotification(errorMessage);

    }
}
