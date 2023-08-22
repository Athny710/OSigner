package org.oefa.gob.pe.osigner.task.platform;

import javafx.concurrent.Task;
import org.oefa.gob.pe.Oefa.Pdf;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.FileUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.StringUtil;

public class ConvertFilesTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        Pdf pdf = new Pdf();

        int count = 1;
        for(FileModel file : SignConfiguration.getInstance().getFilesToSign()){

            if(!StringUtil.isPdfName(file.getName())) {
                String name = pdf.convertToPdf(file.getLocation(), file.getName());
                FileUtil.deleteFile(file.getLocation() + file.getName());
                file.setName(name);
            }

            double progress = (double) count / SignConfiguration.getInstance().getFilesToSign().size();
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_CONVERT.getInitialValue(),
                    Constant.PROGRESS_VALUE_CONVERT.getPartialValue()  * progress
            );
            count++;
        }

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
        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
