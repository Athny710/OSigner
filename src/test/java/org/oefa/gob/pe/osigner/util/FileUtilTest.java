package org.oefa.gob.pe.osigner.util;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.domain.FileModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        FileUtil.zipFiles("",zipName, List.of(fileModel, fileModel2));
    }

    @Test
    void modificationDate(){
        String crlFileName = "X509CRL.crl";
        File checkFile = new File(FileUtil.getTempFolder() + crlFileName);
        long date = checkFile.lastModified();
        String dateString = Constant.DATE_FORMAT.format(checkFile.lastModified());

        Date l1 = new Date(checkFile.lastModified());
        Date l2 = new Date();
        DateUtils.isSameDay(l1, l2);
        if(checkFile.exists() && DateUtils.isSameDay(l1, l2)){
                System.out.println("gaa");

        }
    }
}