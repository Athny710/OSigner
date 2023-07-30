package org.oefa.gob.pe.osigner.core;

import org.oefa.gob.pe.osigner.application.NotificationService;
import org.oefa.gob.pe.osigner.application.ProgressService;

public class NotificationFX {

    public static void showProgressNotification(String title, String message, double progress){
        AppFX.showProgressNotification();
        ProgressService.buildProgressNotification(title, message, progress);
    }

    public static void showCrlErrorNotification(String errorMessage){
        AppFX.showNotificationError();
        NotificationService.buildCrlError(errorMessage);

    }
    public static void showMaterialFxErrorNotification(String errorMessage){
        AppFX.showNotificationError();
        NotificationService.buildMaterialFxError(errorMessage);
    }

    public static void showSignInformationErrorNotification(String errorMessage){
        AppFX.showNotificationError();
        NotificationService.buildSignInformationError(errorMessage);
    }
}
