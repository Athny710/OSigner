package org.oefa.gob.pe.osigner.core;

import org.oefa.gob.pe.osigner.application.NotificationService;

public class NotificationFX {

    public static void showCrlErrorNotification(String errorMessage){
        AppFX.showNotificationError();
        NotificationService.showCrlError(errorMessage);

    }

    public static void showSignInformationErrorNotification(String errorMessage){
        AppFX.showNotificationError();
        NotificationService.showSignInformationError(errorMessage);
    }
}
