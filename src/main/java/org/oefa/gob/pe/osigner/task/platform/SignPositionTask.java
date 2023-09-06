package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.SignatureUtil;

public class SignPositionTask extends Task<Void>{

    @Override
    protected Void call() throws Exception {
        SignatureUtil.setCoordenadasPosicionFirma(
                SignConfiguration.getInstance().getFilesToSign(),
                SignConfiguration.getInstance().getSignProcessConfiguration()
        );
        return null;
    }


    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se obtuvo la posición de firma de los archivos.", this.getClass().getName());
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
        LogUtil.setError(
                "Error obteniendo la posición de firma.",
                this.getClass().getName(),
                (Exception) super.getException()
        );

        StepComponent.showStepError(0);
        NotificationFX.closeProgressNotification();
        NotificationFX.showSignaturePositionErrorNotification(super.getException().getMessage());

    }
}
