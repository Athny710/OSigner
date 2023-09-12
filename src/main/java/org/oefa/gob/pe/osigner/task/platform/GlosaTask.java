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
import org.oefa.gob.pe.osigner.util.OefaUtil;

import java.nio.file.Files;
import java.nio.file.Paths;

public class GlosaTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {

        OefaUtil.addGlosaToFilesToSign(
                SignConfiguration.getInstance().getFilesToSign()
        );

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
        NotificationFX.showSkippedFilesErrorNotification(super.getException().getMessage());

    }
}
