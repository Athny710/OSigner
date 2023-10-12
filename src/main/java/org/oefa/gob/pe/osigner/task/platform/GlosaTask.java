package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.commons.AppProcess;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.OefaUtil;

public class GlosaTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("[TASK] Agregando glosa de verificación", this.getClass().getName());

        OefaUtil.addGlosaToFilesToSign(
                SignConfiguration.getInstance().getFilesToSign()
        );
        return null;

    }


    @Override
    protected void succeeded() {
        LogUtil.setInfo("[PROCCESS] Proceso de obtención de información completado, esperando confirmación del usuario", this.getClass().getName());

        NotificationFX.closeProgressNotification();
        Platform.runLater(()-> {
            StepComponent.showStepCompleted(0);
            PlatformService.disableButtons(false);
        });
        super.succeeded();

    }

    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error agregando la glosa de verificación a los archivos.",
                this.getClass().getName(),
                (Exception) super.getException()
        );
        StepComponent.showStepError(0);
        NotificationFX.closeProgressNotification();
        NotificationFX.showSkippedFilesErrorNotification(super.getException().getMessage(), AppProcess.GLOSA);

    }
}
