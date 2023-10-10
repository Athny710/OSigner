package org.oefa.gob.pe.osigner.core;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.application.NotificationService;
import org.oefa.gob.pe.osigner.application.ProgressService;
import org.oefa.gob.pe.osigner.commons.AppType;

public class NotificationFX {


    private static String title;
    private static String message;
    private static double progress;


    public static void initializeAndShowProgressNotification(String t, String msg){
        if(!isProgressFeatureActive())
            return;

        title = t;
        message = msg;
        progress = 0.0;

        AppFX.showProgressNotification();
        ProgressService.buildProgressNotification(title, message, progress);

    }


    public static void updateProgressNotification(String msg){
        if(!isProgressFeatureActive())
            return;

        message = msg;
        ProgressService.buildProgressNotification(title, msg, progress);

    }


    public static void updateProgressNotification(String msg, double pgrs){
        if(!isProgressFeatureActive())
            return;

        ProgressService.buildProgressNotification(title, msg, pgrs);

    }


    public static void updateProgressNotification(double baseProgress, double partialProgress){
        if(!isProgressFeatureActive())
            return;

        progress = baseProgress + partialProgress;
        ProgressService.buildProgressNotification(title, message, progress);
        if(progress == 1)
            closeProgressNotification();

    }

    public static void closeProgressNotification(){
        if(!isProgressFeatureActive())
            return;

        ProgressService.closeProgressNotification();

    }


    public static void showCrlErrorNotification(String errorMessage){
        if(!isProgressFeatureActive())
            return;

        AppFX.showNotificationError();
        NotificationService.buildCrlError(errorMessage);

    }


    public static void showMaterialFxErrorNotification(String errorMessage){
        if(!isProgressFeatureActive())
            return;

        AppFX.showNotificationError();
        NotificationService.buildMaterialFxError(errorMessage);

    }


    public static void showFatalErrorNotification(String errorMessage){
        AppFX.showNotificationError();
        NotificationService.buildFatalError(errorMessage);

    }


    public static void showSkippedFilesErrorNotification(String errorMessage){
        if(!isProgressFeatureActive())
            return;

        AppFX.showNotificationError();
        NotificationService.buildSkippedFilesError(errorMessage);

    }


    public static void showCertificateErrorNotification(String errorMessage){
        AppFX.showNotificationError();
        NotificationService.buildCertificateError(errorMessage);
    }

    private static boolean isProgressFeatureActive(){
        return AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN);

    }


}
