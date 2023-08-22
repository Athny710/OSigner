package org.oefa.gob.pe.osigner.task.platform;

import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.FileUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class UnzipFilesTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {

        String directory = FileUtil.unzipFiles(SignConfiguration.getInstance().getSignProcessConfiguration().getZipUUID() + ".zip");
        SignConfiguration.getInstance()
                .getFilesToSign()
                .forEach(x ->
                        x.setLocation(directory)
                );

        return null;
    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se descomprimieron los archivos.", this.getClass().getName());
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
        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
