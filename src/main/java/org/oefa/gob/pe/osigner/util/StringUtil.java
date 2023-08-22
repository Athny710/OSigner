package org.oefa.gob.pe.osigner.util;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import java.util.ArrayList;
import java.util.Collections;

public class StringUtil {

    /**
     * Función que se encarga de obtener los parámetros enviados en la ejecución de la aplicación
     * @param url URL que se ejecuta en el navegador con el siguiente formato {CUSTOM_PROTOCOL}://{PARAMETER1}%{PARAMETER2}%{PARAMETER3}%.../
     * @return Retorna un arreglo con los parámetros enviados en la URL
     */
    public static ArrayList<String> getParametersFromUrl(String url){
        ArrayList<String> paramsList = new ArrayList<>();

        String concatenatedParams =  url
                .replaceFirst(AppConfiguration.getKey("CUSTOM_PROTOCOL") +  "://", "")
                .replaceFirst("/", "");

        String[] params = concatenatedParams.split("%");
        Collections.addAll(paramsList, params);

        return paramsList;

    }

    public static boolean isPdfName(String fileName){
        int index = fileName.lastIndexOf('.');
        String extension = fileName.substring(index);

        return extension.toUpperCase().contains("PDF");
    }
}
