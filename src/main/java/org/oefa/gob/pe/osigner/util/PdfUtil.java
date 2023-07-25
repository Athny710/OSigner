package org.oefa.gob.pe.osigner.util;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.LoaderFX;
import org.oefa.gob.pe.osigner.domain.CharCoordinates;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.oefa.gob.pe.osigner.domain.SignCoordinates;

import javax.swing.text.html.Option;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class PdfUtil {

    private static final String OSIGNER_DIRECTORY = System.getProperty("user.home") + AppConfiguration.getKey("OSIGNER_FOLDER");
    private static final String TO_SIGN_FOLDER = AppConfiguration.getKey("POR_FIRMAR_FOLDER");

    public static void convertFilesToPDF(List<FileModel> filesToSign) throws Exception{
        License license = new License();
        license.setLicense(LoaderFX.loadResource("SSignerProd.lic"));

        for(FileModel file : filesToSign) {
            OutputStream returnValue = new ByteArrayOutputStream();
            OutputStream out = new ByteArrayOutputStream();

            if (getFileNameExtension(file.getName()).equals(Constant.DOC_EXTENSION) || getFileNameExtension(file.getName()).equals(Constant.DOCX_EXTENSION)){
                Document document = new Document(file.getLocation());
                String docxName = file.getName();
                document.save(
                        changeExtensionToPdf(file)
                );

                new File(OSIGNER_DIRECTORY + TO_SIGN_FOLDER + docxName).delete();
            }
        }

    }


    public static void setCoordenadasPosicionFirma(List<FileModel> filesToSign, int signatureType, int signatureStyle, int positionType, String usernameSSFD) throws Exception{
        if(signatureStyle == Constant.FIRMA_ESTILO_INVISIBLE)
            return;

        if(signatureType == Constant.FIRMA_TIPO_FIRMA){
            if(positionType == Constant.FIRMA_POSICION_AUTOMATICA)
                setCoordenadasByEtiqueta(filesToSign, usernameSSFD);

            if(positionType == Constant.FIRMA_POSICION_RELATIVA){}
                //setCoordenadasByPositionRelativa();

        }else {
            setCoordenadasByNumeroVisado(filesToSign);

        }

    }

    private static void setCoordenadasByEtiqueta(List<FileModel> filesToSign, String usernameSSFD) throws Exception{
        for(FileModel file : filesToSign){
            InputStream is = new FileInputStream(file.getLocation());
            PDDocument document = PDDocument.load(is);
            PDFTextStripper pdfTextStripper = new CharCoordinates();

            float[] coordenadas = {-1.0F, -1.0F, -1.0F};
            String etiqueta = "[" + usernameSSFD + "]";
            ((CharCoordinates)pdfTextStripper).setTag(etiqueta);

            loop:
            for(int k=1; k<=document.getNumberOfPages(); k++){
                pdfTextStripper.setStartPage(k);
                pdfTextStripper.setEndPage(k);

                String pageText = pdfTextStripper.getText(document);
                String[] lines = pageText.split("\\r?\\n");

                for(int i=0; i<lines.length; i++){
                    if(lines[i].toUpperCase().contains(etiqueta.toUpperCase())){
                        String[] temp = lines[i].split("\\Q][\\E");

                        for(int j=0; j<temp.length; j++){
                            if(temp[j].toUpperCase().contains(etiqueta.toUpperCase())){
                                String[] temp2 = temp[j].split(",");
                                coordenadas = new float[] {
                                        Float.valueOf(temp2[0].replace("[", "").replace("]", "")).floatValue() - 20.0F,
                                        document.getPage(k - 1).getBBox().getHeight() - Float.valueOf(temp2[1].replace("[", "").replace("]", "")).floatValue() - 6.0F,
                                        k
                                };
                                break loop;
                            }
                        }
                    }
                }
            }

            file.setPositionX(coordenadas[0]);
            file.setPositionY(coordenadas[1]);
            file.setPage((int) coordenadas[2]);

        }

    }

    private static void setCoordenadasByPositionRelativa(List<FileModel> filesToSign, int relativePosition) throws Exception{
        for(FileModel file : filesToSign){
            Optional<SignCoordinates> coordinatesOpt = Constant.UbicacionFirma.relativePosition
                    .stream()
                    .filter(x -> x.getType() == relativePosition)
                    .findFirst();

            if(coordinatesOpt.isPresent()){
                file.setPositionX((float) coordinatesOpt.get().getPosX());
                file.setPositionY((float) coordinatesOpt.get().getPosY());
            }
        }
    }

    private static void setCoordenadasByNumeroVisado(List<FileModel> filesToSign) throws Exception{
        List<SignCoordinates> coordinatesList = new ArrayList<>();

        for(FileModel file : filesToSign){
            PdfReader reader = new PdfReader(file.getLocation());
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

    }

    public static void addGlosaVerificacion(List<FileModel> filesToSign, SignConfiguration signConfiguration) throws Exception{
        fixPagesOrientation(filesToSign);
        generateAndAddGlosa(filesToSign, signConfiguration);
    }


    private static void generateAndAddGlosa(List<FileModel> filesToSign, SignConfiguration signConfiguration) throws Exception{
        /*
        for(FileModel file : filesToSign) {
            PdfReader reader = new PdfReader(file.getLocation());
            com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
            com.lowagie.text.Document document2 = new com.lowagie.text.Document(PageSize.A4);

            int numeroDePaginas = reader.getNumberOfPages();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance((com.lowagie.text.Document) document, (OutputStream)baos);
            writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
            document.open();
            PdfContentByte pcb = writer.getDirectContent();

            for (int i = 1; i < numeroDePaginas + 1; ++i) {
                PdfImportedPage page = writer.getImportedPage(reader, i);
                document.setPageSize(reader.getPageSize(i));
                document.newPage();
                pcb.addTemplate((PdfTemplate)page, 0.0f, 0.0f);
            }
            document.setPageSize(document2.getPageSize());
            document.newPage();
            writer.setPageEmpty(false);
            String textoGlosa = signConfiguration.getSignProcessConfiguration().getGlosaText()
                    .replace("[clave]", file.getClaveVerificacion())
                    .replace("[urlQR]", signConfiguration.getSignProcessConfiguration().getGlosaUrl());


            posX = (document.getPageSize().getWidth() - 500.0f) / 2.0f;
            posY = document.top() - 64.0f;

            if (this.qr == null) {
                urlQR = urlQR + "/?clave=" + clave;
                this.qr = new Qr(this.x, this.y, this.ancho, this.alto, urlQR);
                this.qr.setxExtra(2.0f);
                this.qr.setyExtra(2.0f);
            }
            if (this.codidgoBarras == null) {
                this.codidgoBarras = new CodidgoBarras(pcb, String.valueOf(codigoOperacion));
                this.codidgoBarras.setxExtra(63.0f);
                this.codidgoBarras.setyExtra(3.0f);
            }
            this.generar(pcb, this.qr, this.codidgoBarras);
            reader.close();
            document.close();
            writer.close();
            return baos.toByteArray();
        }

         */
    }

    private static void fixPagesOrientation(List<FileModel> filesToSign) throws Exception{
        for(FileModel file : filesToSign) {
            String fileToSignLocation = file.getLocation();
            String fileToSignName = file.getName();

            com.lowagie.text.pdf.PdfReader reader = new PdfReader(fileToSignLocation);
            FileOutputStream returnValue = new FileOutputStream(getTempName(file));
            com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
            com.lowagie.text.pdf.PdfWriter writer = com.lowagie.text.pdf.PdfWriter.getInstance((com.lowagie.text.Document) document, (OutputStream) returnValue);
            writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
            document.open();
            PdfContentByte content = writer.getDirectContent();

            block5:for (int i = 1; i <= reader.getNumberOfPages(); ++i) {
                PdfImportedPage page = writer.getImportedPage(reader, i);
                int pageRotation = reader.getPageRotation(i);
                switch (pageRotation) {
                    case 90: {
                        Rectangle rect = new Rectangle(page.getHeight(), page.getWidth());
                        document.setPageSize(rect);
                        document.newPage();
                        content.addTemplate((PdfTemplate) page, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, page.getHeight());
                        continue block5;
                    }
                    case 180: {
                        Rectangle rect = new Rectangle(page.getWidth(), page.getHeight());
                        document.setPageSize(rect);
                        document.newPage();
                        content.addTemplate((PdfTemplate) page, -1.0f, 0.0f, 0.0f, -1.0f, page.getWidth(), page.getHeight());
                        continue block5;
                    }
                    case 270: {
                        Rectangle rect = new Rectangle(page.getHeight(), page.getWidth());
                        document.setPageSize(rect);
                        document.newPage();
                        content.addTemplate((PdfTemplate) page, 0.0f, 1.0f, -1.0f, 0.0f, page.getHeight(), 0.0f);
                        continue block5;
                    }
                    default: {
                        Rectangle rect = new Rectangle(page.getWidth(), page.getHeight());
                        document.setPageSize(rect);
                        document.newPage();
                        content.addTemplate((PdfTemplate) page, 0.0f, 0.0f);
                    }
                }
            }
            document.close();
            reader.close();
            returnValue.close();

            Path fileToSignPath = Paths.get(file.getLocation());
            Files.move(fileToSignPath, fileToSignPath.resolveSibling(fileToSignName), REPLACE_EXISTING);
            file.setName(fileToSignName);
            file.setLocation(OSIGNER_DIRECTORY + TO_SIGN_FOLDER + fileToSignName);

        }
    }




    private static String getTempName(FileModel file){
        int pos = file.getName().lastIndexOf('.');
        String pdfName = file.getName().substring(0, pos) + Constant.TEMP_FILE_NAME_EXTENSION;
        String pdfLocation = OSIGNER_DIRECTORY + TO_SIGN_FOLDER + pdfName;

        file.setName(pdfName);
        file.setLocation(pdfLocation);

        return pdfLocation;

    }



    private static String changeExtensionToPdf(FileModel file){
        int pos = file.getName().lastIndexOf('.');
        String pdfName =  file.getName().substring(0, pos) + Constant.PDF_EXTENSION;
        String pdfLocation = OSIGNER_DIRECTORY + TO_SIGN_FOLDER + pdfName;

        file.setName(pdfName);
        file.setLocation(pdfLocation);

        return pdfLocation;

    }

    private static String getFileNameExtension(String filename){
        int pos = filename.lastIndexOf('.');
        return filename.substring(pos).toUpperCase();

    }
}
