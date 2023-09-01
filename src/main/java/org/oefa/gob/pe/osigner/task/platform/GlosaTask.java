package org.oefa.gob.pe.osigner.task.platform;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import javafx.concurrent.Task;
import org.oefa.gob.pe.Oefa.Glosa;
import org.oefa.gob.pe.Oefa.domain.Dimension;
import org.oefa.gob.pe.Oefa.domain.GlosaPeru;
import org.oefa.gob.pe.Oefa.domain.GlosaSSFD;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.FileUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

import java.nio.file.Files;
import java.nio.file.Paths;

public class GlosaTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        Rectangle pageSize = PageSize.A4;
        float x = (pageSize.getWidth() - Constant.GLOSA_SSFD_WIDTH) / 2;
        float y = (pageSize.getHeight() - Constant.GLOSA_SSFD_HEIGTH);
        int margin = Constant.GLOSA_MARGIN;

        Dimension containerDimension = new Dimension(x, y - margin, 500, 70, 1);
        Dimension qrDimension = new Dimension(x + margin, y - margin + 3, 100, 100, 64.0f);
        Dimension barcodeDimension = new Dimension(x + 100, y - margin + 5, 0, 0, 75.0f);

        int total = SignConfiguration.getInstance().getFilesToSign().stream().filter(FileModel::isAddGlosa).toList().size();
        int count = 1;

        for(FileModel file : SignConfiguration.getInstance().getFilesToSign()){
            if(file.isAddGlosa()){
                GlosaSSFD glosaSSFD = new GlosaSSFD(
                        SignConfiguration.getInstance().getSignProcessConfiguration().getGlosaText(),
                        SignConfiguration.getInstance().getSignProcessConfiguration().getGlosaUrl(),
                        String.valueOf(file.getCodigoOperacion())
                );
                GlosaPeru glosaPeru = new GlosaPeru(
                        Constant.GLOSA_PERU_TEXT_TOP,
                        Constant.GLOSA_PERU_TEXT_BOTTOM,
                        Constant.GLOSA_PERU_TEXT_URL
                );
                Glosa.MARGIN = Constant.GLOSA_MARGIN;

                byte[] bytesIn = Files.readAllBytes(Paths.get(file.getLocation() + file.getName()));
                byte[] bytesTmp = Glosa.generateGlosa(bytesIn, glosaSSFD, containerDimension, qrDimension, barcodeDimension);
                byte[] bytesOut = Glosa.generateGlosaVertical(bytesTmp, glosaPeru);

                file.setBytes(bytesOut);
                FileUtil.saveFileBytes(file);

                double progress = (double) count/ total;
                NotificationFX.updateProgressNotification(
                        Constant.PROGRESS_VALUE_GLOSA.getInitialValue(),
                        Constant.PROGRESS_VALUE_GLOSA.getPartialValue() * progress
                );
                count++;

            }

        }

        return null;

    }


    @Override
    protected void succeeded() {
        LogUtil.setInfo("Se agregó la glosa de verificación a los archivos.", this.getClass().getName());
        NotificationFX.updateProgressNotification("Obteniendo posición de firma.");
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
        NotificationFX.showSignInformationErrorNotification(errorMessage);

    }
}
