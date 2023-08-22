package org.oefa.gob.pe.osigner.util;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import org.oefa.gob.pe.Oefa.Pdf;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignCoordinates;
import org.oefa.gob.pe.osigner.domain.SignProcessModel;

import java.util.List;
import java.util.Optional;


public class SignatureUtil {


    public static void setCoordenadasPosicionFirma(List<FileModel> filesToSign, SignProcessModel signProcessModel) throws Exception{

        if(signProcessModel.getSignatureType() != Constant.FIRMA_TIPO_FIRMA){
            setCoordenadasByNumeroVisado(filesToSign);
            return;
        }

        if(signProcessModel.getSignaturePositionType() == Constant.FIRMA_POSICION_RELATIVA) {
            setCoordenadasByPositionRelativa(filesToSign, signProcessModel.getSignatureRelativePosition());
            return;
        }

        if(signProcessModel.getSignaturePositionType() == Constant.FIRMA_POSICION_AUTOMATICA) {
            String label = "[" + signProcessModel.getUsernameSSFD() + "]";
            int count = 1;
            for (FileModel fileModel : filesToSign) {
                float[] coordinates = Pdf.getSignaturePosition(label, fileModel.getLocation() + fileModel.getName());
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
            }

            double progress = (double) index/filesToSign.size();
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getInitialValue(),
                    Constant.PROGRESS_VALUE_SIGNATURE_POS.getPartialValue() * progress
            );
        }
    }


    private static void setCoordenadasByNumeroVisado(List<FileModel> filesToSign) throws Exception{
        List<SignCoordinates> coordinatesList;
        int index = 0;

        for(FileModel file : filesToSign){
            com.lowagie.text.pdf.PdfReader reader = new PdfReader(file.getLocation());
            int signaturesNumber = reader.getAcroFields().getSignatureNames().size();

            Rectangle pageRectangle = reader.getPageSize(1);
            if(pageRectangle.getHeight() > pageRectangle.getWidth()){
                if(signaturesNumber > 8)
                    throw new Exception("Ya se pusieron en el documento el numero maximo de vistos bueno permitidos en formato horizontal: 8");

                coordinatesList = Constant.UbicacionFirma.visadoHorizontalPosition;

            }else{
                if(signaturesNumber > 12)
                    throw new Exception("Ya se pusieron en el documento el numero maximo de vistos bueno permitidos en formato vertical: 12");

                coordinatesList = Constant.UbicacionFirma.visadoVerticalPosition;

            }

            Optional<SignCoordinates> coordinatesOpt = coordinatesList.
                    stream()
                    .filter(x -> x.getType() == signaturesNumber)
                    .findFirst();

            if(coordinatesOpt.isPresent()){
                file.setPositionX((float) coordinatesOpt.get().getPosX());
                file.setPositionY((float) coordinatesOpt.get().getPosY());
                file.setPage(1);
            }

        }

        double progress = (double) index / filesToSign.size();
        NotificationFX.updateProgressNotification(
                Constant.PROGRESS_VALUE_SIGNATURE_POS.getInitialValue(),
                Constant.PROGRESS_VALUE_SIGNATURE_POS.getPartialValue() * progress
        );

    }



}
