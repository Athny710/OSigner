package org.oefa.gob.pe.osigner.core;

import org.oefa.gob.pe.osigner.MainApplication;

public class ResourceFX {
    public static String load(String resource){
        return MainApplication.class.getResource(resource).toString();
    }
}
