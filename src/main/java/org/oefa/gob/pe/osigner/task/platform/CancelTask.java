package org.oefa.gob.pe.osigner.task.platform;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.application.RestService;
import org.oefa.gob.pe.osigner.core.AppFX;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class CancelTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        LogUtil.setInfo("[TASK] Cancelando el proceso de firma", this.getClass().getName());

        RestService.cancelSignProcess(
                SignConfiguration.getInstance()
        );

        return null;

    }


    @Override
    protected void succeeded() {
        super.succeeded();
        Platform.runLater(() -> {
            AppFX.showMessageAndCloseApplication(false);
        });

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error cancelando el proceso de firma",
                this.getClass().getName(),
                (Exception) super.getException()
        );
        AppFX.showMessageAndCloseApplication(false);

    }
}
