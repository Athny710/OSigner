package org.oefa.gob.pe.osigner.task.platform;

import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.OefaUtil;

public class ConvertFilesTask extends Task<Void> {


    @Override
    protected Void call() throws Exception {
        OefaUtil.convertFilesToSignToPDF(
                SignConfiguration.getInstance().getFilesToSign()
        );

        return null;
    }


    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se convirtieron los archivos a formato PDF.", this.getClass().getName());
        NotificationFX.updateProgressNotification("Agregando glosa de verificación.");
        super.succeeded();

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error obteniendo convirtiendo archivos a formato PDF.",
                this.getClass().getName(),
                (Exception) super.getException()
        );

        StepComponent.showStepError(0);
        NotificationFX.closeProgressNotification();

        if (OefaUtil.FILES_WITH_ERRORS.isEmpty()) {
            NotificationFX.showFatalErrorNotification(errorMessage);
        } else {
            NotificationFX.showSkippedFilesErrorNotification(super.getException().getMessage());
        }

    }


}
