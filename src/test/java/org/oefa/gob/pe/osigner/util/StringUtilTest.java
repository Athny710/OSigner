package org.oefa.gob.pe.osigner.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

class StringUtilTest {

    @Test
    void getParametersFromUrl() {
        String text = "PRUEBA AUTORIZACION";
        String[] parts = text.split(" ");
        Optional<String> opt = Arrays.stream(parts).filter(x -> x.equals("AUT")).findFirst();
        if(opt.isPresent())
            System.out.println("=================================");
    }
}