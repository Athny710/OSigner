package org.oefa.gob.pe.osigner.core;

import javafx.application.Application;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.StringUtil;
import java.util.ArrayList;

public class RouterFX {

    public static void intializeApp(Application.Parameters parameters) {
        try {
            ArrayList<String> params = StringUtil.getParametersFromUrl(parameters.getRaw().get(0));
            String key = params.get(0);

            if (params.size() == 1) {
                AppConfiguration.APP_TYPE = AppType.FULL_SIGN;
                initFullApp();
            }
            if (params.size() == 2) {
                AppConfiguration.APP_TYPE = AppType.SIMPLE_SIGN;
                AppConfiguration.ID_CLIENT = params.get(0);
                AppConfiguration.ID_GROUP = params.get(1);
                initSimpleSignProcessByPlatform();
            }

            if (params.size() == 3) {
                if (key.equals(AppConfiguration.getKey("FIRMA_MASIVA_KEY"))) {
                    AppConfiguration.APP_TYPE = AppType.MASSIVE_SIGN;
                    AppConfiguration.ID_CLIENT = params.get(1);
                    AppConfiguration.ID_GROUP = params.get(2);
                    initMassiveSignProcessByPlatform();

                } else if (key.equals(AppConfiguration.getKey("VERIFICA_KEY"))) {
                    AppConfiguration.APP_TYPE = AppType.VERIFY_SIGN;

                }
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

}
