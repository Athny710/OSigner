package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.FileUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class DownloadFilesTask extends Task<Void> {


    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("[TASK] Descargando archivos", this.getClass().getName());

        // Tiempo de espera necesario para que se muestre la interfaz de usuario antes de que inicie el proceso
        Thread.sleep(Constant.INITIALIZATION_APP_DELAY_TIME);
        if(AppConfiguration.APP_TYPE.equals(AppType.SIMPLE_SIGN)){
            for(FileModel fileToSave : SignConfiguration.getInstance().getFilesToSign()){
                fileToSave.setLocation(
                        FileUtil.saveFileBytes(fileToSave)
                );
            }
        }

        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN)){
            FileUtil.saveFileFromUrlWithProgress(
                    SignConfiguration.getInstance().getSignProcessConfiguration().getDownloadRestService(),
                    SignConfiguration.getInstance().getSignProcessConfiguration().getZipUUID() + ".zip"
            );
        }

        return null;

    }


    @Override
    protected void succeeded() {
        NotificationFX.updateProgressNotification("Descomprimiendo archivos.");
        if(AppConfiguration.APP_TYPE.equals(AppType.SIMPLE_SIGN)) {
            Platform.runLater(() -> {
                StepComponent.showStepCompleted(0);
                PlatformService.disableButtons(false);
            });
        }
        super.succeeded();

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error descargando los archivos.",
                this.getClass().getName(),
                (Exception) super.getException()
        );
        StepComponent.showStepError(0);
        NotificationFX.closeProgressNotification();
        NotificationFX.showFatalErrorNotification(errorMessage);

    }

}
