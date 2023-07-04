package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.application.RestService;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfigurationModel;
import org.oefa.gob.pe.osigner.infra.output.adapter.IText7Adapter;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class SignInformationTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("Obteniendo información de firma", this.getClass().getName());
        SignConfigurationModel.createInstance(
                RestService.getSignConfigurationModel()
        );
        return null;

    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se obtuvo información del proceso de firma", this.getClass().getName());
        super.succeeded();

    }

    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
        "Error obteniendo el proceso de firma",
                this.getClass().getName(),
                (Exception) super.getException()
        );
        StepComponent.showStepError(0);
        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
