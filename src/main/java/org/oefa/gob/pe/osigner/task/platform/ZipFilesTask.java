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
        LogUtil.setInfo("[TASK] Comprimiendo archivos firmados", this.getClass().getName());

        FileUtil.zipFiles(
                FileUtil.getSignedFolder(),
                SignConfiguration.getInstance().getSignProcessConfiguration().getZipUUID() + ".zip",
                SignConfiguration.getInstance().getFilesToSign()
        );

        return null;
    }

    @Override
    protected void succeeded() {
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
        StepComponent.showStepError(3);
        NotificationFX.closeProgressNotification();
        NotificationFX.showFatalErrorNotification(errorMessage);

    }
}
