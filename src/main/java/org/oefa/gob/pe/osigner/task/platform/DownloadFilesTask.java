package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.FileUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class DownloadFilesTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        Thread.sleep(1000);
        if(AppConfiguration.APP_TYPE.equals(AppType.SIMPLE_SIGN)){
            for(FileModel fileToSave : SignConfiguration.getInstance().getFilesToSign()){
                fileToSave.setLocation(
                        FileUtil.saveFileBytes(fileToSave)
                );
            }

        }else{
            String url = SignConfiguration.getInstance().getSignProcessConfiguration().getDownloadRestService();
            String zipPath = FileUtil.saveFileFromUrl(url);
            String directoryPath = FileUtil.unzipFiles(zipPath);

            SignConfiguration.getInstance()
                    .getFilesToSign()
                    .forEach(x ->
                            x.setLocation(directoryPath + x.getName())
                    );

        }
        return null;

    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se descargaron los archivos", this.getClass().getName());
        super.succeeded();

        Platform.runLater(() -> {
            StepComponent.showStepCompleted(0);
            PlatformService.disableButtons(false);

        });

    }

    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error descargando los archivos de firma",
                this.getClass().getName(),
                (Exception) super.getException()
        );
        StepComponent.showStepError(0);
        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
