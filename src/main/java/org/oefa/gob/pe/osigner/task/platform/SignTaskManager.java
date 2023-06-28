package org.oefa.gob.pe.osigner.task.platform;

import org.oefa.gob.pe.osigner.core.component.CertificateComponent;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.fx.PlatformModel;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.TaskUtil;

public class SignTaskManager {

    public static void initializeSignProccess(PlatformModel platformModel){
        StepComponent.STEP_LIST = platformModel.getSteps();
        CertificateComponent.CERT_COMBOBOX = platformModel.getCertificateComboBox();
        startSignProccess();
    }

    private static void startSignProccess(){
        LogUtil.setInfo("Iniciando con el proceso de firma", SignTaskManager.class.getName());
        SignInformationTask signInformationTask = new SignInformationTask();
        TaskUtil.executeTask(signInformationTask);
    }

    private static void completeSignProccess(){

    }

}
