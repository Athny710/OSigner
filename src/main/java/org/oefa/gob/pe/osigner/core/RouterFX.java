package org.oefa.gob.pe.osigner.core;

import javafx.application.Application;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.StringUtil;

import java.util.ArrayList;

public class RouterFX {

    private static final String FIRMA_KEY = AppConfiguration.getKey("FIRMA_KEY");
    private static final String VERIFICA_KEY = AppConfiguration.getKey("VERIFICA_KEY");


    public static void intializeApp(Application.Parameters parameters){
        try {
            ArrayList<String> params = StringUtil.getParametersFromUrl(parameters.getRaw().get(0));
            String key = params.get(0);

            if(key.equals(FIRMA_KEY)){
                AppConfiguration.ID_CLIENT = params.get(1);
                AppConfiguration.ID_GROUP = params.get(2);
                initSignAppByPlatform();

            }else if(key.equals(VERIFICA_KEY)){
                initVerificaAppByPlatform();

            }else {
                initFullAppByUser();

            }

        } catch (Exception e){
            LogUtil.setError(
                    "Error obteniendo parámetros de ejecución para iniciar la aplicación",
                    AppConfiguration.class.getName(),
                    e
            );

        }
    }

    private static void initSignAppByPlatform() throws Exception {
        AppFX.showPlatformLoader();

    }


    private static void initVerificaAppByPlatform(){

    }


    private static void initFullAppByUser(){

    }

}
