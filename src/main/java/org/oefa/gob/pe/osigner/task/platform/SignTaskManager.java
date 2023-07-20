package org.oefa.gob.pe.osigner.task.platform;

import org.oefa.gob.pe.osigner.core.component.CertificateComponent;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.fx.PlatformModel;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.TaskUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignTaskManager {

    public static void initializeSignProccess(PlatformModel platformModel){
        StepComponent.STEP_LIST = platformModel.getSteps();
        CertificateComponent.CERT_COMBOBOX = platformModel.getCertificateComboBox();

        startSignProccess();

    }

    public static void completeSignProccess(){


    }

    private static void startSignProccess(){
        LogUtil.setInfo("Iniciando con el proceso de firma", SignTaskManager.class.getName());
        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();

        TaskUtil.executeTask(downloadFilesTask);

    }

}
