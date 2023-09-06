package org.oefa.gob.pe.osigner.util;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
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

        if(signProcessModel.getSignatureType() != Constant.FIRMA_TIPO_FIRMA)
            setCoordenadasByNumeroVisado(filesToSign);

        if(signProcessModel.getSignaturePositionType() == Constant.FIRMA_POSICION_RELATIVA)
            setCoordenadasByPositionRelativa(filesToSign, signProcessModel.getSignatureRelativePosition());

        if(signProcessModel.getSignaturePositionType() == Constant.FIRMA_POSICION_AUTOMATICA)
            setCoordenadasByLabel(filesToSign, signProcessModel.getUsernameSSFD());

        if(!FILE_WITH_ERRORS.isEmpty()){
            StringBuilder errorMessage = new StringBuilder("No se pude obtener la posición de firma para los siguientes archivos: ");
            for(String fileName : FILE_WITH_ERRORS){
                errorMessage.append("\n").append(fileName);
            }
            throw new Exception(errorMessage.toString());
        }

    }


    private static void setCoordenadasByPositionRelativa(List<FileModel> filesToSign, int relativePosition) throws Exception{
        int index = 0;
        for(FileModel file : filesToSign){
            Optional<SignCoordinates> coordinatesOpt = Constant.UbicacionFirma.relativePosition
                    .stream()
                    .filter(x -> x.getType() == relativePosition)
                    .findFirst();

            if(coordinatesOpt.isPresent()){
                file.setPositionX((float) coordinatesOpt.get().getPosX());
                file.setPositionY((float) coordinatesOpt.get().getPosY());
            }else{
                LogUtil.setInfo("No se encontró posición relativa para el valor indicado: " + relativePosition, SignatureUtil.class.getName());
                FILE_WITH_ERRORS.add(file.getName());
            }

            double progress = (double) index/filesToSign.size();
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getInitialValue(),
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getPartialValue() * progress
            );

            index++;
        }
    }


    private static void setCoordenadasByNumeroVisado(List<FileModel> filesToSign) throws Exception{
        List<SignCoordinates> coordinatesList;
        int index = 0;

        for(FileModel file : filesToSign) {
            com.lowagie.text.pdf.PdfReader reader = new PdfReader(file.getLocation());
            int signaturesNumber = reader.getAcroFields().getSignatureNames().size();

            Rectangle pageRectangle = reader.getPageSize(1);
            if (pageRectangle.getHeight() > pageRectangle.getWidth()) {
                if (signaturesNumber > 8) {
                    LogUtil.setInfo("Ya se pusieron en el documento el número máximo de vistos buenos permitidos en formato horizontal (8)", SignatureUtil.class.getName());
                    FILE_WITH_ERRORS.add(file.getName());
                }

                coordinatesList = Constant.UbicacionFirma.visadoHorizontalPosition;

            } else {
                if (signaturesNumber > 12) {
                    LogUtil.setInfo("Ya se pusieron en el documento el número máximo de vistos buenos permitidos en formato vertical (12)", SignatureUtil.class.getName());
                    FILE_WITH_ERRORS.add(file.getName());
                }

                coordinatesList = Constant.UbicacionFirma.visadoVerticalPosition;

            }

            Optional<SignCoordinates> coordinatesOpt = coordinatesList.
                    stream()
                    .filter(x -> x.getType() == signaturesNumber)
                    .findFirst();

            if (coordinatesOpt.isPresent()) {
                file.setPositionX((float) coordinatesOpt.get().getPosX());
                file.setPositionY((float) coordinatesOpt.get().getPosY());
                file.setPage(1);
            }

            double progress = (double) index / filesToSign.size();
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getInitialValue(),
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getPartialValue() * progress
            );

            index++;
        }

    }

    public static void setCoordenadasByLabel(List<FileModel> filesToSign, String textLabel) throws Exception{
        String label = "[" + textLabel + "]";
        int count = 1;
        for (FileModel fileModel : filesToSign) {
            float[] coordinates = Pdf.getSignaturePosition(label, fileModel.getLocation() + fileModel.getName());

            if(coordinates[0] == -1 || coordinates[1] == -1 || coordinates[2] == -1)
                FILE_WITH_ERRORS.add(fileModel.getName());

            fileModel.setPositionX(coordinates[0]);
            fileModel.setPositionY(coordinates[1]);
            fileModel.setPage((int) coordinates[2]);

            double progress = (double) count / filesToSign.size();
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getInitialValue(),
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getPartialValue() * progress
            );
            count++;
        }

    }



}
