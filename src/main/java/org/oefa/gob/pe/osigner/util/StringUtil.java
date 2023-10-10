package org.oefa.gob.pe.osigner.util;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * Función que se encarga de obtener los parámetros enviados en la ejecución de la aplicación
     * @param url URL que se ejecuta en el navegador con el siguiente formato {CUSTOM_PROTOCOL}://{PARAMETER1}%{PARAMETER2}%{PARAMETER3}%.../
     * @return Retorna un arreglo con los parámetros enviados en la URL
     */
    public static ArrayList<String> getParametersFromUrl(String url){
        LogUtil.setInfo("Parámetros de ejecución obtenidos: " + url, StringUtil.class.getName());
        ArrayList<String> paramsList = new ArrayList<>();

        String concatenatedParams = url.split(Pattern.quote("://"))[1].replaceFirst("/", "");

        String[] params = concatenatedParams.split("%");
        for(String p : params){
            LogUtil.setInfo("Parámetro obtenido: " + p, StringUtil.class.getName());
        }
        Collections.addAll(paramsList, params);

        return paramsList;

    }

    public static boolean isPdfName(String fileName){
        int index = fileName.lastIndexOf('.');
        String extension = fileName.substring(index);

        return extension.toUpperCase().contains("PDF");
    }


    public static String formatString(String string){
        return new String(string.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

    }


    public static boolean isCertificateValidToSign(String alias){
        String[] parts = alias.split(" ");
        Optional<String> opt = Arrays.stream(parts).filter(x -> x.equals("AUT") || x.equals("CIF")).findFirst();
        return opt.isEmpty();

    }
}
