package org.oefa.gob.pe.osigner.application;

import javafx.collections.FXCollections;
import javafx.scene.Parent;
import org.oefa.gob.pe.osigner.core.AnimationFX;
import org.oefa.gob.pe.osigner.core.AppFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.CertificateModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.domain.fx.PlatformModel;
import org.oefa.gob.pe.osigner.task.platform.SignTaskManager;
import org.oefa.gob.pe.osigner.util.CertificateUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

import java.util.List;

public class PlatformService {

    public static PlatformModel PLATFORM_MODEL;

    public static void initializeSignProccess(){
        loadAllCertificates();
        SignTaskManager.initializeSignProccess(PLATFORM_MODEL);

    }

    public static void completeSignProccess(String certificateAlias){
        try {
            StepComponent.showStepCompleted(1);
            SignTaskManager.completeSignProccess(
                    CertificateUtil.getCertificateToSignByAlias(certificateAlias)
            );
            disableButtons(true);
        }catch (Exception e){

        }

    }

    public static void reloadCertificates(){

    }

    public static void loadAllCertificates(){
        LogUtil.setInfo("Cargando los certificados", PlatformService.class.getName());
        List<String> certificateListByUserDNI = CertificateUtil.getUserCertificateList(SignConfiguration.getInstance().getSignProcessConfiguration().getUserDNI());
        List<String> list = certificateListByUserDNI.size() == 0 ? List.of("No se encontraron certificados") : certificateListByUserDNI;

        PLATFORM_MODEL.getCertificateComboBox().getItems().clear();
        PLATFORM_MODEL.getCertificateComboBox().setItems(FXCollections.observableArrayList(list));

    }

    public static void disableButtons(boolean disable){
        PLATFORM_MODEL.getConfirmButton().setDisable(disable);
        PLATFORM_MODEL.getUpdateButton().setDisable(disable);

    }

    public static void showSuccessAndClose(){
        showEndView("view/PlatformSuccess.fxml");

    }

    public static void showErrorAndClose(){
        showEndView("view/PlatformError.fxml");

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
