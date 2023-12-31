package org.oefa.gob.pe.osigner.Configuration;

import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.core.ResourceFX;
import org.oefa.gob.pe.osigner.util.LogUtil;
import java.io.InputStream;
import java.util.Properties;

public class AppConfiguration {

    public static AppType APP_TYPE;
    public static String ID_CLIENT;
    public static String ID_GROUP;
    public static String DNI_CLIENT;


    public static String getKey(String key){
        Properties properties = new Properties();
        try {
            InputStream resourceStream = ResourceFX.loadResource("application.properties");
            properties.load(resourceStream);

            return properties.getProperty(key);

        } catch (Exception e){
            LogUtil.setError(
                    "Error obteniendo propiedad " + key + " del archivo de configuración",
                    AppConfiguration.class.getName(),
                    e
            );
            return "";
        }
    }
}
