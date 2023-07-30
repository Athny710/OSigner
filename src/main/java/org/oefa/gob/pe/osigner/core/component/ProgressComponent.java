package org.oefa.gob.pe.osigner.core.component;

import org.oefa.gob.pe.osigner.application.NotificationService;
import org.oefa.gob.pe.osigner.application.ProgressService;

public class ProgressComponent {

    private static String title;
    private static String message;
    private static double progress;

    public static void initializeAndShowProgress(String t, String msg, double pgrs){
        title = t;
        message = msg;
        progress = pgrs;
        build();

    }

    public static void updateMessageAndProgress(String msg, double pgrs){
        message = msg;
        progress = pgrs;
        build();
    }

    public static void updateProgress(double pgrs){
        progress = pgrs;
        build();
    }

    private static void build(){
        ProgressService.buildProgressNotification(
                title, message, progress
        );
    }
}
