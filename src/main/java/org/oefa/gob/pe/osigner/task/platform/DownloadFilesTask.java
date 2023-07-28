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
import org.oefa.gob.pe.osigner.util.PdfUtil;

public class DownloadFilesTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("Obteniendo los archivos", this.getClass().getName());
        Thread.sleep(500);

        if(AppConfiguration.APP_TYPE.equals(AppType.SIMPLE_SIGN)){
            for(FileModel fileToSave : SignConfiguration.getInstance().getFilesToSign()){
                fileToSave.setLocation(
                        FileUtil.saveFileBytes(fileToSave)
                );
            }
        }

        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN)){
            LogUtil.setInfo("Descargando zip", this.getClass().getName());
            String zipPath = FileUtil.saveFileFromUrl(
                    SignConfiguration.getInstance().getSignProcessConfiguration().getDownloadRestService(),
                    SignConfiguration.getInstance().getSignProcessConfiguration().getZipName()
            );

            LogUtil.setInfo("Descomprimiendo archivos", this.getClass().getName());
            String directoryPath = FileUtil.unzipFiles(zipPath);
            SignConfiguration.getInstance()
                    .getFilesToSign()
                    .forEach(x ->
                            x.setLocation(directoryPath + x.getName())
                    );

            LogUtil.setInfo("Convirtiendo archivos a PDF.", this.getClass().getName());
            PdfUtil.convertFilesToPDF(
                    SignConfiguration.getInstance().getFilesToSign()
            );
            /*
            PdfUtil.addGlosaVerificacion(
                    SignConfiguration.getInstance().getFilesToSign(),
                    SignConfiguration.getInstance().getSignProcessConfiguration().getGlosaVerificacion()
            );

             */

            LogUtil.setInfo("Obteniendo coordenadas de firma.", this.getClass().getName());
            PdfUtil.setCoordenadasPosicionFirma(
                    SignConfiguration.getInstance().getFilesToSign(),
                    SignConfiguration.getInstance().getSignProcessConfiguration().getSignatureType(),
                    SignConfiguration.getInstance().getSignProcessConfiguration().getSignatureStyle(),
                    SignConfiguration.getInstance().getSignProcessConfiguration().getSignaturePositionType(),
                    SignConfiguration.getInstance().getSignProcessConfiguration().getUsernameSSFD()
            );

        }
        return null;

    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("Archivos listos para firmar", this.getClass().getName());
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
                "Error obteniendo los archivos de firma",
                this.getClass().getName(),
                (Exception) super.getException()
        );
        StepComponent.showStepError(0);
        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
