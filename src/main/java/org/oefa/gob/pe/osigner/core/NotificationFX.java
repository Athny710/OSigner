package org.oefa.gob.pe.osigner.core;

import org.oefa.gob.pe.osigner.application.NotificationService;
import org.oefa.gob.pe.osigner.application.ProgressService;

public class NotificationFX {

    private static String title;
    private static String message;
    private static double progress;

    public static void initializeAndShowProgressNotification(String t, String msg, double pgrs){
        title = t;
        message = msg;
        progress = pgrs;

        AppFX.showProgressNotification();
        ProgressService.buildProgressNotification(title, message, progress);
    }

    public static void updateProgressNotification(String msg, double pgrs){
        ProgressService.buildProgressNotification(title, msg, pgrs);
    }

    public static void updateProgressNotification(double pgrs){
        ProgressService.buildProgressNotification(title, message, pgrs);
        if(pgrs == 1)
            closeProgressNotification();
    }

    public static void closeProgressNotification(){
        ProgressService.closeProgressNotification();

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
