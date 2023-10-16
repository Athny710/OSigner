package org.oefa.gob.pe.osigner.core;

import org.oefa.gob.pe.osigner.MainApplication;

import java.io.InputStream;

public class ResourceFX {

    public static InputStream loadResource(String resourceName){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(resourceName);

    }

    public static String load(String resource){
        return String.valueOf(MainApplication.class.getResource(resource));
    }
}
