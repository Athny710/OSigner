package org.oefa.gob.pe.osigner.application;

import javafx.application.Platform;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;
import org.oefa.gob.pe.osigner.domain.fx.NotificationModel;
import org.oefa.gob.pe.osigner.task.configuration.ConfigurationTaskManager;

public class NotificationService {

    public static NotificationModel NOTIFICATION_MODEL;


    public static void buildCrlError(String message){
        NOTIFICATION_MODEL.getTitleLabel().setText("Advertencia");
        NOTIFICATION_MODEL.getTextLabel().setText(message + "\n¿Desea continuar sin verificar los certificados?");

        NOTIFICATION_MODEL.getConfirmButton().setText("Continuar");
        NOTIFICATION_MODEL.getConfirmButton().setOnAction(e -> {
            ApplicationModel.NOTIFICATION_STAGE.close();
            PlatformService.loadAllCertificates();
            ConfigurationTaskManager.setConfigurationFinished();
        });

        NOTIFICATION_MODEL.getCancelButton().setOnAction(e -> {
            PlatformService.showErrorAndClose();
        });

    }

    public static void buildSignInformationError(String message){
        NOTIFICATION_MODEL.getTitleLabel().setText("Error");
        NOTIFICATION_MODEL.getTextLabel().setText(message + "\nNo es posible continuar con el proceso de firma.");

        NOTIFICATION_MODEL.getConfirmButton().setText("Aceptar");
        NOTIFICATION_MODEL.getConfirmButton().setOnAction(e -> {
            ApplicationModel.NOTIFICATION_STAGE.close();
            PlatformService.showErrorAndClose();
        });

        NOTIFICATION_MODEL.getCancelButton().setVisible(false);

    }

    public static void buildMaterialFxError(String message){

    }
}
