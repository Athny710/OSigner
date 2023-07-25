package org.oefa.gob.pe.osigner.core;

import java.io.InputStream;

public class LoaderFX {

    public static InputStream loadResource(String resourceName){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(resourceName);

    }


}
