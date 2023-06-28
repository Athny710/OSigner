package org.oefa.gob.pe.osigner.application;

import javafx.application.Platform;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;
import org.oefa.gob.pe.osigner.domain.fx.NotificationModel;
import org.oefa.gob.pe.osigner.task.configuration.ConfigurationTaskManager;

public class NotificationService {

    public static NotificationModel NOTIFICATION_MODEL;

    public static void showCrlError(String message){
        NOTIFICATION_MODEL.getTitleLabel().setText("Advertencia");
        NOTIFICATION_MODEL.getTextLabel().setText(message + "\n¿Desea continuar sin verificar los certificados?");

        NOTIFICATION_MODEL.getConfirmButton().setText("Continuar");
        NOTIFICATION_MODEL.getConfirmButton().setOnAction(e -> {
            ApplicationModel.NOTIFICATION_STAGE.close();
            PlatformService.loadAllCertificates();
            ConfigurationTaskManager.setConfigurationFinished();
        });

        NOTIFICATION_MODEL.getCancelButton().setOnAction(e -> {
            Platform.exit();
        });

    }

    public static void showSignInformationError(String message){
        NOTIFICATION_MODEL.getTitleLabel().setText("Error");
        NOTIFICATION_MODEL.getTextLabel().setText(message + "\nNo es posible continuar con el proceso de firma.");

        NOTIFICATION_MODEL.getConfirmButton().setText("Confirmar");
        NOTIFICATION_MODEL.getConfirmButton().setOnAction(e -> {
            Platform.exit();
        });

        NOTIFICATION_MODEL.getCancelButton().setVisible(false);

    }
}
