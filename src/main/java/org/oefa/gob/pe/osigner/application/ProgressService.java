package org.oefa.gob.pe.osigner.application;

import javafx.application.Platform;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;
import org.oefa.gob.pe.osigner.domain.fx.NotificationModel;

public class ProgressService {
    public static NotificationModel PROGRESS_MODEL;

    public static void buildProgressNotification(String title, String message, double progress){
        Platform.runLater(() -> {
            PROGRESS_MODEL.getTitleLabel().setText(title);
            PROGRESS_MODEL.setTextLabelProperty(message);
            PROGRESS_MODEL.setProgressBarProperty(progress);
        });
    }

    public static void closeProgressNotification(){
        Platform.runLater(() -> {
            ApplicationModel.PROGRESS_STAGE.close();
        });

    }
}
