package org.oefa.gob.pe.osigner.task.configuration;

import org.oefa.gob.pe.osigner.application.PlatformLoaderService;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.TaskUtil;


public class ConfigurationTaskManager {


    private static int configurationTasksNumber = 3;


    /**
     * Función que se encarga de llevar el contral de las tareas de configuración
     */
    public static void initializeConfigurationTask() {
        LogUtil.setInfo("[CONFIGURACION] Iniciando la configuración de la aplicación", ConfigurationTaskManager.class.getName());

        MaterialConfigurationTask materialConfigurationTask = new MaterialConfigurationTask();
        CertificateConfigurationTask certificateConfigurationTask = new CertificateConfigurationTask();
        SignProcessConfigurationTask signProcessConfigurationTask = new SignProcessConfigurationTask();

        TaskUtil.executeTask(certificateConfigurationTask);
        TaskUtil.executeTask(materialConfigurationTask);
        TaskUtil.executeTask(signProcessConfigurationTask);

    }


    /**
     * Función que se encarga de iniciar el módulo de firma cuando las configuraciones se hayan terminado.
     */
    public static void setConfigurationFinished(){
        configurationTasksNumber -= 1;
        System.out.println(configurationTasksNumber);
        if(configurationTasksNumber == 0){
            PlatformLoaderService.showUserInterface();

        }

    }
}
