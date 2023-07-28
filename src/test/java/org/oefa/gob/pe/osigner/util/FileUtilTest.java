package org.oefa.gob.pe.osigner.util;

import org.junit.jupiter.api.Test;
import org.oefa.gob.pe.osigner.domain.FileModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    @Test
    void zipFiles() throws Exception {
        String zipName = "zipTest.zip";
        FileModel fileModel = new FileModel();
        FileModel fileModel2 = new FileModel();
        fileModel.setLocation("C:\\Users\\71489974\\Desktop\\Pruebas\\200MB PDF File.pdf");
        fileModel2.setLocation("C:\\Users\\71489974\\Desktop\\Pruebas\\200MB PDF File - copia.pdf");
        fileModel.setName("200MB PDF File.pdf");
        fileModel2.setName("200MB PDF File - copia.pdf");

        FileUtil.zipFiles(zipName, List.of(fileModel, fileModel2));
    }
}