package org.oefa.gob.pe.osigner.task.platform;

import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.FileUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class ZipFilesTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        FileUtil.zipFiles(
                SignConfiguration.getInstance().getSignProcessConfiguration().getZipUUID() + ".zip",
                SignConfiguration.getInstance().getFilesToSign()
        );

        return null;
    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se comprimieron los archivos.", this.getClass().getName());
        NotificationFX.updateProgressNotification("Subiendo archivos firmados");
        super.succeeded();

    }

    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error comprimiendo los archivos",
                this.getClass().getName(),
                (Exception) super.getException()
        );
        StepComponent.showStepError(4);
        NotificationFX.closeProgressNotification();
        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
