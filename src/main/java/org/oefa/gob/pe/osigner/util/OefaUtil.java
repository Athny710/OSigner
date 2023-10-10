package org.oefa.gob.pe.osigner.util;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import org.oefa.gob.pe.Oefa.Glosa;
import org.oefa.gob.pe.Oefa.Pdf;
import org.oefa.gob.pe.Oefa.domain.Dimension;
import org.oefa.gob.pe.Oefa.domain.GlosaPeru;
import org.oefa.gob.pe.Oefa.domain.GlosaSSFD;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OefaUtil {


    public static ArrayList<String> FILES_WITH_ERRORS = new ArrayList<>();


    public static void convertFilesToSignToPDF(List<FileModel> filesToSign) throws Exception{
        reset();
        convertToPdf(filesToSign);

        if(!FILES_WITH_ERRORS.isEmpty()){
            StringBuilder errorMessage = new StringBuilder("No se pudo convertir a PDF los siguientes archivos: ");
            for(String fileName : FILES_WITH_ERRORS){
                errorMessage.append("\n").append(" - ").append(fileName);
            }
            throw new Exception(errorMessage.toString());

        }
    }

    public static void addGlosaToFilesToSign(List<FileModel> filesToSign) throws Exception {
        reset();
        addGlosaToFile(filesToSign);

        if(!FILES_WITH_ERRORS.isEmpty()){
            StringBuilder errorMessage = new StringBuilder("No se pudo agregar la glosa de verificación a los siguientes archivos: ");
            for(String fileName : FILES_WITH_ERRORS){
                errorMessage.append("\n").append(" - ").append(fileName);
            }
            throw new Exception(errorMessage.toString());
        }
    }


    /**
     * Función que se encarga de convertir archivos a PDF.
     * @param filesToSign Archivos que se desean firmar.
     * @throws Exception Excepción con la librerío o licencia.
     */
    private static void convertToPdf(List<FileModel> filesToSign) throws Exception{
        Pdf pdf = new Pdf();

        int count = 1;
        for(FileModel file : filesToSign){
            try {
                if (!StringUtil.isPdfName(file.getName())) {
                    String name = pdf.convertToPdf(file.getLocation(), file.getName());
                    FileUtil.deleteFile(file.getLocation() + file.getName());
                    file.setName(name);
                }
            }catch (Exception e){
                LogUtil.setError("Error convirtiendo a PDF el archivo: " + file.getName(), OefaUtil.class.getName(), e);
                FILES_WITH_ERRORS.add(file.getName());
                file.setPage(Constant.FIRMA_PAGINA_ERROR);
            }

            double progress = (double) count / SignConfiguration.getInstance().getFilesToSign().size();
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_CONVERT.getInitialValue(),
                    Constant.PROGRESS_VALUE_CONVERT.getPartialValue()  * progress
            );
            count++;
        }
    }


    /**
     * Función que se encarga de agregar las glosas de verificación en los archivos PDF.
     * @param filesToSign Lista de archivos que se desean firmar.
     */
    private static void addGlosaToFile(List<FileModel> filesToSign) {
        Rectangle pageSize = PageSize.A4;
        float x = (pageSize.getWidth() - Constant.GLOSA_SSFD_WIDTH) / 2;
        float y = (pageSize.getHeight() - Constant.GLOSA_SSFD_HEIGTH);
        int margin = Constant.GLOSA_MARGIN;

        Dimension containerDimension = new Dimension(x, y - margin, 500, 70, 1);
        Dimension qrDimension = new Dimension(x + margin, y - margin + 3, 100, 100, 64.0f);
        Dimension barcodeDimension = new Dimension(x + 100, y - margin + 5, 0, 0, 75.0f);

        int total = filesToSign.size();
        int count = 1;

        for(FileModel file : SignConfiguration.getInstance().getFilesToSign()){
            if(file.getPage() == Constant.FIRMA_PAGINA_ERROR)
                continue;

            if(file.isAddGlosa()){
                try {
                    GlosaSSFD glosaSSFD = new GlosaSSFD(
                            SignConfiguration.getInstance().getSignProcessConfiguration().getGlosaText(),
                            SignConfiguration.getInstance().getSignProcessConfiguration().getGlosaUrl(),
                            String.valueOf(file.getCodigoOperacion())
                    );
                    GlosaPeru glosaPeru = new GlosaPeru(
                            Constant.GLOSA_PERU_TEXT_TOP,
                            Constant.GLOSA_PERU_TEXT_BOTTOM,
                            Constant.GLOSA_PERU_TEXT_URL
                    );
                    Glosa.MARGIN = Constant.GLOSA_MARGIN;

                    byte[] bytesIn = Files.readAllBytes(Paths.get(file.getLocation() + file.getName()));
                    byte[] bytesTmp = Glosa.generateGlosa(bytesIn, glosaSSFD, containerDimension, qrDimension, barcodeDimension);
                    byte[] bytesOut = Glosa.generateGlosaVertical(bytesTmp, glosaPeru);

                    file.setBytes(bytesOut);
                    FileUtil.saveFileBytes(file);

                }catch (Exception e){
                    LogUtil.setError("Error agregando glosa de verificación al archivo: " + file.getName(), OefaUtil.class.getName(), e);
                    FILES_WITH_ERRORS.add(file.getName());
                    file.setPage(Constant.FIRMA_PAGINA_ERROR);

                }
            }

            double progress = (double) count/ total;
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_GLOSA.getInitialValue(),
                    Constant.PROGRESS_VALUE_GLOSA.getPartialValue() * progress
            );
            count++;

        }
    }

    private static void reset(){
        FILES_WITH_ERRORS.clear();

    }



}
