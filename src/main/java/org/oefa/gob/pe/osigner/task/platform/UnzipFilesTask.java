package org.oefa.gob.pe.osigner.task.platform;

import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.FileUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class UnzipFilesTask extends Task<Void> {


    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("[TASK] Descomprimiendo archivos", this.getClass().getName());

        FileUtil.unzipFiles(SignConfiguration.getInstance().getSignProcessConfiguration().getZipUUID() + ".zip");
        SignConfiguration.getInstance()
                .getFilesToSign()
                .forEach(x ->
                        x.setLocation(FileUtil.getTempFolder())
                );

        return null;
    }


    @Override
    protected void succeeded() {
        NotificationFX.updateProgressNotification("Convirtiendo archivos a PDF.");
        super.succeeded();

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error descomprimiendo los archivos",
                this.getClass().getName(),
                (Exception) super.getException()
        );
        StepComponent.showStepError(0);
        NotificationFX.closeProgressNotification();
        NotificationFX.showFatalErrorNotification(errorMessage);

    }

}
