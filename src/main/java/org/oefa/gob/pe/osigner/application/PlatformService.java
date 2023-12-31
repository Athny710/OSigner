package org.oefa.gob.pe.osigner.application;

import javafx.collections.FXCollections;
import javafx.scene.Parent;
import org.oefa.gob.pe.osigner.core.AnimationFX;
import org.oefa.gob.pe.osigner.core.AppFX;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.domain.fx.PlatformModel;
import org.oefa.gob.pe.osigner.task.platform.SignTaskManager;
import org.oefa.gob.pe.osigner.util.CertificateUtil;
import org.oefa.gob.pe.osigner.util.LogUtil;

import java.util.List;

public class PlatformService {


    public static PlatformModel PLATFORM_MODEL;

    /**
     * Función que se encarga de cargar los certificados e iniciar con el proceso de firma.
     */
    public static void initializeSignProccess(){
        loadAllCertificates();
        SignTaskManager.initializeSignProccess(PLATFORM_MODEL);

    }


    /**
     * Función que se llama cuando el usuario hace click en el botón de "Confirmar".
     * @param certificateAlias Alias de certificado seleccionado por el usuario.
     */
    public static void completeSignProccess(String certificateAlias){
        if(certificateAlias == null) {
            showErrorCertificateMessage(true);
            return;
        }

        SignTaskManager.completeSignProccess(certificateAlias);

    }


    public static void cancelSignProccess(){
        SignTaskManager.cancelSignProccess();

    }


    /**
     * Función que se encarga de actualizar los certificados encontrados y cargarlos en el Combo Box.
     */
    public static void reloadCertificates(){
        try {
            LogUtil.setInfo("Actualizando los certificados", PlatformService.class.getName());
            List<String> certificateListByUserDNI = CertificateUtil.reloadCertificateList(SignConfiguration.getInstance().getSignProcessConfiguration().getUserDNI());
            List<String> list = certificateListByUserDNI.isEmpty() ? List.of("No se encontraron certificados") : certificateListByUserDNI;

            PLATFORM_MODEL.getCertificateComboBox().getItems().clear();
            PLATFORM_MODEL.getCertificateComboBox().setItems(FXCollections.observableArrayList(list));

        }catch (Exception e){
            LogUtil.setError(
                    "Error al actualizar los certificados",
                    PlatformService.class.getName(),
                    e
            );
        }

    }


    /**
     * Función que se encarga de cargar los certifiados encontrados en el Combo Box.
     */
    public static void loadAllCertificates(){
        LogUtil.setInfo("Cargando los certificados", PlatformService.class.getName());
        List<String> certificateListByUserDNI = CertificateUtil.getUserCertificateList(SignConfiguration.getInstance().getSignProcessConfiguration().getUserDNI());
        List<String> list = certificateListByUserDNI.isEmpty() ? List.of("No se encontraron certificados") : certificateListByUserDNI;

        PLATFORM_MODEL.getCertificateComboBox().getItems().clear();
        PLATFORM_MODEL.getCertificateComboBox().setItems(FXCollections.observableArrayList(list));

    }


    /**
     * Función que se encarga de habilitar/deshabilitar los botones de la interfaz (Confirmar/Actualizar).
     * @param disable Habiitar o desabilitar.
     */
    public static void disableButtons(boolean disable){
        PLATFORM_MODEL.getConfirmButton().setDisable(disable);
        PLATFORM_MODEL.getUpdateButton().setDisable(disable);

    }


    public static void showErrorCertificateMessage(boolean value){
        PLATFORM_MODEL.getErrorCertificateLabel().setVisible(value);

    }


}
