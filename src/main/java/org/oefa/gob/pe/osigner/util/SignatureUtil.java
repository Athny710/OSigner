package org.oefa.gob.pe.osigner.util;

import com.lowagie.text.Rectangle;
import org.oefa.gob.pe.Oefa.Pdf;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignCoordinates;
import org.oefa.gob.pe.osigner.domain.SignProcessModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SignatureUtil {


    private static final ArrayList<String> FILE_WITH_ERRORS = new ArrayList<>();


    public static void setCoordenadasPosicionFirma(List<FileModel> filesToSign, SignProcessModel signProcessModel) throws Exception{
        reset();

        if(signProcessModel.getSignatureType() == Constant.FIRMA_TIPO_VISADO)
            setCoordenadasByNumeroVisado(filesToSign);

        if(signProcessModel.getSignatureType() == Constant.FIRMA_TIPO_FIRMA){
            if(signProcessModel.getSignaturePositionType() == Constant.FIRMA_POSICION_RELATIVA)
                setCoordenadasByPositionRelativa(filesToSign, signProcessModel.getSignatureRelativePosition());

            if(signProcessModel.getSignaturePositionType() == Constant.FIRMA_POSICION_AUTOMATICA)
                setCoordenadasByLabel(filesToSign, signProcessModel.getUsernameSSFD());

        }

        if(!FILE_WITH_ERRORS.isEmpty()){
            StringBuilder errorMessage = new StringBuilder("No se pude obtener la posición de firma para los siguientes archivos: ");
            for(String fileName : FILE_WITH_ERRORS){
                errorMessage.append("\n").append(" - ").append(fileName);
            }
            throw new Exception(errorMessage.toString());
        }

    }


    /**
     * Función que asigna coordenadas X, Y de la posición de firma utilizando una posición relativa.
     * @param filesToSign Lista de archivos que se desean firmar.
     * @param relativePosition Posición relativa de la firma. Existen 9 posiciones relativas en un archivo
     */
    private static void setCoordenadasByPositionRelativa(List<FileModel> filesToSign, int relativePosition){
        int index = 1;
        for(FileModel file : filesToSign){
            if(file.getPage() == Constant.FIRMA_PAGINA_ERROR)
                continue;

            Optional<SignCoordinates> coordinatesOpt = Constant.UbicacionFirma.relativePosition
                    .stream()
                    .filter(x -> x.getType() == relativePosition)
                    .findFirst();

            if(coordinatesOpt.isPresent()){
                file.setPositionX((float) coordinatesOpt.get().getPosX());
                file.setPositionY((float) coordinatesOpt.get().getPosY());
                file.setPage(Constant.FIRMA_PAGINA_DEFECTO);
            }else{
                LogUtil.setInfo("No se encontró posición relativa: " + relativePosition + " en el archivo: " + file.getName(), SignatureUtil.class.getName());
                FILE_WITH_ERRORS.add(file.getName());
                file.setPage(Constant.FIRMA_PAGINA_ERROR);
            }

            double progress = (double) index/filesToSign.size();
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getInitialValue(),
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getPartialValue() * progress
            );

            index++;
        }
    }


    /**
     * Función que asigna coordenadas X, Y de la posición de visado utilizando la cantidad de visados existentes en el archivo.
     * @param filesToSign Lista de archivos que se desean firmar.
     */
    private static void setCoordenadasByNumeroVisado(List<FileModel> filesToSign){
        List<SignCoordinates> coordinatesList;
        int index = 1;

        for(FileModel file : filesToSign) {
            if(file.getPage() == Constant.FIRMA_PAGINA_ERROR)
                continue;

            try {
                com.lowagie.text.pdf.PdfReader reader = new com.lowagie.text.pdf.PdfReader(file.getLocation());
                int signaturesNumber = reader.getAcroFields().getSignatureNames().size();

                Rectangle pageRectangle = reader.getPageSize(1);
                if (pageRectangle.getHeight() > pageRectangle.getWidth()) {
                    if (signaturesNumber > 8) {
                        LogUtil.setInfo("Ya se pusieron en el documento el número máximo de vistos buenos permitidos en formato horizontal (8)", SignatureUtil.class.getName());
                        FILE_WITH_ERRORS.add(file.getName());
                    }

                    coordinatesList = Constant.UbicacionFirma.visadoHorizontalPosition;

                } else {
                    if (signaturesNumber >= 12) {
                        LogUtil.setInfo("Ya se pusieron en el documento el número máximo de vistos buenos permitidos en formato vertical (12)", SignatureUtil.class.getName());
                        FILE_WITH_ERRORS.add(file.getName());
                    }

                    coordinatesList = Constant.UbicacionFirma.visadoVerticalPosition;

                }

                Optional<SignCoordinates> coordinatesOpt = coordinatesList.
                        stream()
                        .filter(x -> x.getType() == (signaturesNumber + 1))
                        .findFirst();

                if (coordinatesOpt.isPresent()) {
                    file.setPositionX((float) coordinatesOpt.get().getPosX());
                    file.setPositionY((float) coordinatesOpt.get().getPosY());
                    file.setPage(Constant.FIRMA_PAGINA_DEFECTO);
                } else {
                    file.setPage(Constant.FIRMA_PAGINA_ERROR);
                }

            }catch (Exception e){
                LogUtil.setError("Error obteniendo el archivo: " + file.getName(), SignatureUtil.class.getName(), e);
                FILE_WITH_ERRORS.add(file.getName());
                file.setPage(Constant.FIRMA_PAGINA_ERROR);
            }

            double progress = (double) index / filesToSign.size();
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getInitialValue(),
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getPartialValue() * progress
            );

            index++;
        }

    }


    /**
     * Función que asigna la posición X, Y de la posición de firma utilizando una etiqueta como referencia.
     * @param filesToSign Lista de archivos que se desean firmar.
     * @param textLabel Texto que se encuentra adento de la etiqueta.
     * @throws Exception
     */
    public static void setCoordenadasByLabel(List<FileModel> filesToSign, String textLabel) {
        String label = "[" + textLabel + "]";
        int index = 1;
        for (FileModel fileModel : filesToSign) {
            if(fileModel.getPage() == Constant.FIRMA_PAGINA_ERROR)
                continue;

            try {
                float[] coordinates = Pdf.getSignaturePosition(label, fileModel.getLocation() + fileModel.getName());

                if (coordinates[0] == -1 || coordinates[1] == -1 || coordinates[2] == -1) {
                    LogUtil.setInfo("No se encontró etiqueta: " + label + " en el archivo: " + fileModel.getName(), SignatureUtil.class.getName());
                    FILE_WITH_ERRORS.add(fileModel.getName());
                    fileModel.setPage(Constant.FIRMA_PAGINA_ERROR);
                } else {
                    fileModel.setPositionX(coordinates[0]);
                    fileModel.setPositionY(coordinates[1]);
                    fileModel.setPage((int) coordinates[2]);
                }
            }catch (Exception e){
                LogUtil.setError("Error obteniendo el archivo: " + fileModel.getName(), SignatureUtil.class.getName(), e);
                FILE_WITH_ERRORS.add(fileModel.getName());
                fileModel.setPage(Constant.FIRMA_PAGINA_ERROR);
            }

            double progress = (double) index/ filesToSign.size();
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getInitialValue(),
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getPartialValue() * progress
            );

            index++;

        }
    }


    private static void reset(){
        FILE_WITH_ERRORS.clear();

    }

}
