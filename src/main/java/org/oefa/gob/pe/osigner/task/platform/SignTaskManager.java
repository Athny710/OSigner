package org.oefa.gob.pe.osigner.task.platform;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.CertificateComponent;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.CertificateModel;
import org.oefa.gob.pe.osigner.domain.fx.PlatformModel;
import org.oefa.gob.pe.osigner.util.TaskUtil;
import java.util.List;

public class SignTaskManager {



    public static void initializeSignProccess(PlatformModel platformModel){
        StepComponent.STEP_LIST = platformModel.getSteps();
        CertificateComponent.CERT_COMBOBOX = platformModel.getCertificateComboBox();

        startSignProccess();

    }


    private static void startSignProccess(){
        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN))
            NotificationFX.initializeAndShowProgressNotification(
                    "Obteniendo archivos",
                    "Descargando archivos..."
            );

        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        TaskUtil.executeTask(downloadFilesTask);

    }

    public static void completeSignProccess(CertificateModel certificateModel){
        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN))
            NotificationFX.initializeAndShowProgressNotification(
                    "Completando proceso",
                    "Firmando archivos..."
            );

        SignFilesTask signFilesTask = new SignFilesTask(certificateModel);
        UploadFilesTask uploadFilesTask = new UploadFilesTask();

        TaskUtil.executeTwoTasksOnSerial(List.of(signFilesTask, uploadFilesTask));

    }


}
