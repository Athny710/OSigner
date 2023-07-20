package org.oefa.gob.pe.osigner.task.configuration;

import org.oefa.gob.pe.osigner.application.PlatformLoaderService;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.TaskUtil;

public class ConfigurationTaskManager {

    private static int configurationTasksNumber = 3;

    public static void initializeConfigurationTask() {
        LogUtil.setInfo("[CONFIGURACION] Iniciando la configuración de la aplicación", ConfigurationTaskManager.class.getName());

        MaterialConfigurationTask materialConfigurationTask = new MaterialConfigurationTask();
        CertificateConfigurationTask certificateConfigurationTask = new CertificateConfigurationTask();
        SignProcessConfigurationTask signProcessConfigurationTask = new SignProcessConfigurationTask();

        TaskUtil.executeTask(certificateConfigurationTask);
        TaskUtil.executeTask(materialConfigurationTask);
        TaskUtil.executeTask(signProcessConfigurationTask);

    }

    public static void setConfigurationFinished(){
        configurationTasksNumber -= 1;
        if(configurationTasksNumber == 0){
            PlatformLoaderService.showUserInterface();

        }

    }
}
