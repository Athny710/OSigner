package org.oefa.gob.pe.osigner.application;

import javafx.scene.Parent;
import org.oefa.gob.pe.osigner.core.AnimationFX;
import org.oefa.gob.pe.osigner.core.AppFX;
import org.oefa.gob.pe.osigner.domain.fx.PlatformModel;
import org.oefa.gob.pe.osigner.task.platform.SignTaskManager;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class PlatformService {

    public static PlatformModel PLATFORM_MODEL;

    public static void initializeSignProccess(){
        SignTaskManager.initializeSignProccess(PLATFORM_MODEL);

    }

    public static void completeSignProccess(){
        SignTaskManager.completeSignProccess();
        disableButtons(true);

    }

    public static void reloadCertificates(){

    }

    public static void loadAllCertificates(){
        LogUtil.setInfo("Cargando todos los certificados", PlatformService.class.getName());
    }

    public static void disableButtons(boolean disable){
        PLATFORM_MODEL.getConfirmButton().setDisable(disable);
        PLATFORM_MODEL.getUpdateButton().setDisable(disable);

    }

    public static void showSuccessAndClose(){
        showEndView("view/PlatformSuccess.fxml");

    }

    public static void showErrorAndClose(){
        showEndView("view/PlatformSuccess.fxml");

    }

    private static void showEndView(String resource){
        try {
            Parent platformParent = AppFX.getParent(resource);
            PlatformLoaderService.platformLoaderModel.getMainContainer().getChildren().add(2, platformParent);

            AnimationFX.displayEndView(platformParent);
            AppFX.closeApplication();

        }catch (Exception e){
            LogUtil.setError(
                    "Error cargando interfaz de usuario",
                    PlatformLoaderService.class.getName(),
                    e
            );
        }
    }


}
