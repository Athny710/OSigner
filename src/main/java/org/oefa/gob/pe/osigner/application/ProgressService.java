package org.oefa.gob.pe.osigner.application;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.oefa.gob.pe.osigner.core.AppFX;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;
import org.oefa.gob.pe.osigner.domain.fx.NotificationModel;

public class ProgressService {
    public static NotificationModel NOTIFICATION_MODEL;

    public static void buildProgressNotification(String title, String message, double progress){
        Platform.runLater(() -> {
            NOTIFICATION_MODEL.getTitleLabel().setText(title);
            NOTIFICATION_MODEL.setTextLabelProperty(message);
            NOTIFICATION_MODEL.setProgressBarProperty(progress);
        });
    }

    public static void closeProgressNotification(){
        Platform.runLater(() -> {
            ApplicationModel.NOTIFICATION_STAGE.close();
        });

    }
}
