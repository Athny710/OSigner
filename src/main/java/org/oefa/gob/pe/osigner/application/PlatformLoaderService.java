package org.oefa.gob.pe.osigner.application;

import javafx.scene.Parent;
import org.oefa.gob.pe.osigner.core.AnimationFX;
import org.oefa.gob.pe.osigner.core.AppFX;
import org.oefa.gob.pe.osigner.domain.fx.PlatformLoaderModel;
import org.oefa.gob.pe.osigner.task.configuration.ConfigurationTaskManager;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class PlatformLoaderService {

    public static PlatformLoaderModel platformLoaderModel;

    /**
     * Función que inicia la configuración de la aplicación.
     */
    public static void initializeConfiguration(){
        ConfigurationTaskManager.initializeConfigurationTask();

    }


    /**
     * Función que muestra la interfaz de firma.
     */
    public static void showUserInterface(){
        try {
            Parent platformParent = AppFX.getParent("view/Platform.fxml");

            platformLoaderModel.getMainContainer().getChildren().add(0, platformParent);
            AnimationFX.displayPlatformView(platformLoaderModel.getPlatformLoaderContainer());

            // Se inicia con el proceso de firma.
            PlatformService.initializeSignProccess();

        }catch (Exception e){
            LogUtil.setError(
                    "Error cargando interfaz de usuario",
                    PlatformLoaderService.class.getName(),
                    e
            );
        }
    }

}
