package org.oefa.gob.pe.osigner.application;

import javafx.stage.Stage;
import org.oefa.gob.pe.osigner.core.AppFX;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;
import org.oefa.gob.pe.osigner.domain.fx.NotificationModel;

public class ProgressService {
    public static NotificationModel NOTIFICATION_MODEL;
    private static Stage PROGRESS_STAGE;

    public static void buildProgressNotification(String title, String message, double progress){
        NOTIFICATION_MODEL.getTitleLabel().setText(title);
        NOTIFICATION_MODEL.getTextLabel().setText(message);
        NOTIFICATION_MODEL.getProgressBar().setProgress(progress);
    }

    public static void closeProgressNotification(){
        ApplicationModel.NOTIFICATION_STAGE.close();

    }
}
