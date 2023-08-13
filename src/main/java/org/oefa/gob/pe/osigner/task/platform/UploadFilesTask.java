package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.application.RestService;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.FileUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class UploadFilesTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN)) {

            LogUtil.setInfo("Zipeando los archivos firmados.", this.getClass().getName());
            NotificationFX.updateProgressNotification(
                    "Comprimiendo archivos firmados",
                    0.4
            );
            FileUtil.zipFiles(
                    SignConfiguration.getInstance().getSignProcessConfiguration().getZipUUID() + ".zip",
                    SignConfiguration.getInstance().getFilesToSign()
            );

            LogUtil.setInfo("Subiendo los archivos firmados.", this.getClass().getName());
            NotificationFX.updateProgressNotification(
                    "Subiendo archivos firmados",
                    0.8
            );
        }

        RestService.uploadFilesSigned(
                SignConfiguration.getInstance()
        );

        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN))
            NotificationFX.closeProgressNotification();

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
        /*
        String errorMessage = LogUtil.setError(
                "Error firmando los archivos",
                this.getClass().getName(),
                (Exception) super.getException()
        );

         */
        System.out.println(super.getException().getMessage());
        super.getException().printStackTrace();
        StepComponent.showStepError(3);
        //NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
