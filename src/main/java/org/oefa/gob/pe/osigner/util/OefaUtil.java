package org.oefa.gob.pe.osigner.util;

import org.oefa.gob.pe.Oefa.Glosa;
import org.oefa.gob.pe.Oefa.Pdf;
import org.oefa.gob.pe.Oefa.domain.GlosaOefa;
import org.oefa.gob.pe.Oefa.domain.GlosaPeru;

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
        int total = filesToSign.size();
        int count = 1;

        for(FileModel file : SignConfiguration.getInstance().getFilesToSign()){
            if(file.getPage() == Constant.FIRMA_PAGINA_ERROR)
                continue;

            if(file.isAddGlosa()){
                try {
                    GlosaOefa glosaSSFD = new GlosaOefa(
                            StringUtil.generateGlosaText(SignConfiguration.getInstance().getSignProcessConfiguration().getGlosaText(), SignConfiguration.getInstance().getSignProcessConfiguration().getGlosaUrl(), file.getClaveVerificacion()),
                            SignConfiguration.getInstance().getSignProcessConfiguration().getGlosaUrl(),
                            String.valueOf(file.getCodigoOperacion())
                    );
                    GlosaPeru glosaPeru = new GlosaPeru(
                            Constant.GLOSA_PERU_TEXT_TOP,
                            Constant.GLOSA_PERU_TEXT_BOTTOM,
                            Constant.GLOSA_PERU_TEXT_URL
                    );

                    byte[] bytesIn = Files.readAllBytes(Paths.get(file.getLocation() + file.getName()));
                    byte[] bytesTmp = Glosa.generateGlosaOefa(bytesIn, glosaSSFD);
                    byte[] bytesOut = Glosa.generateGlosaFirmaPeru(bytesTmp, glosaPeru);

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
