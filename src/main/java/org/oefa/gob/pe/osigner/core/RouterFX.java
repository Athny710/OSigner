package org.oefa.gob.pe.osigner.core;

import javafx.application.Application;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.StringUtil;
import java.util.ArrayList;

public class RouterFX {

    /**
     * Función que se encarga de decidir el qué módulo se cargará y se le mostrará al usuario.
     * @param parameters Parámetros enviados al ejecutar la aplicación.
     */
    public static void intializeApp(Application.Parameters parameters) {
        try {
            // Se ha ejecutado la aplicación por el usuario como aplicación de escritorio sin parámetros.
            if(parameters.getRaw().isEmpty()) {
                initFullApp();
                return;

            }
            // Se ha ejecutado la aplicación por un navegador; el primer parámetro decidirá qué módulo mostrar.
            ArrayList<String> params = StringUtil.getParametersFromUrl(parameters.getRaw().get(0));
            String moduleKey = params.get(0);

            if(moduleKey.equals(AppConfiguration.getKey("FIRMA_MASIVA_KEY"))){
                AppConfiguration.APP_TYPE = AppType.MASSIVE_SIGN;
                AppConfiguration.ID_CLIENT = params.get(1);
                AppConfiguration.ID_GROUP = params.get(2);
                initMassiveSignProcessByPlatform();

            }else if(moduleKey.equals(AppConfiguration.getKey("VERIFICA_KEY"))){
                AppConfiguration.APP_TYPE = AppType.VERIFY_SIGN;
                initVerificaApp();

            }else{
                AppConfiguration.APP_TYPE = AppType.SIMPLE_SIGN;
                AppConfiguration.ID_CLIENT = params.get(0);
                AppConfiguration.ID_GROUP = params.get(1);
                initSimpleSignProcessByPlatform();

            }

        } catch (Exception e) {
            LogUtil.setError(
                    "Error obteniendo parámetros de ejecución para iniciar la aplicación",
                    AppConfiguration.class.getName(),
                    e
            );
        }

    }


    private static void initMassiveSignProcessByPlatform() throws Exception {
        AppFX.showPlatformLoader();

    }


    private static void initSimpleSignProcessByPlatform() throws Exception {
        AppFX.showPlatformLoader();

    }


    private static void initFullApp(){

    }


    private static void initVerificaApp(){

    }

}
