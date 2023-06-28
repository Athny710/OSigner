package org.oefa.gob.pe.osigner.application;

import javafx.application.Platform;
import org.oefa.gob.pe.osigner.domain.fx.PlatformModel;
import org.oefa.gob.pe.osigner.task.platform.SignTaskManager;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class PlatformService {

    public static PlatformModel PLATFORM_MODEL;

    public static void initializeSignProccess(){
        SignTaskManager.initializeSignProccess(PLATFORM_MODEL);

    }

    public static void closeApplication(){
        LogUtil.setInfo("Cerrando aplicación", PlatformService.class.getName());
        Platform.exit();

    }


    public static void loadAllCertificates(){
        LogUtil.setInfo("Cargando todos los certificados", PlatformService.class.getName());
    }

    public static void signInformationDownloaded(){
        PLATFORM_MODEL.getConfirmButton().setDisable(false);
        PLATFORM_MODEL.getUpdateButton().setDisable(false);
    }
}
