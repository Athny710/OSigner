package org.oefa.gob.pe.osigner.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class StringUtilTest {

    @Test
    void getParametersFromUrl() {
        ArrayList<String> array = StringUtil.getParametersFromUrl("");
        System.out.println(array.get(0));
    }
}