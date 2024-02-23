package org.oefa.gob.pe.osigner.infra.output.adapter;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.*;
import com.lowagie.text.Paragraph;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.ResourceFX;
import org.oefa.gob.pe.osigner.domain.CertificateModel;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.domain.SignProcessModel;
import org.oefa.gob.pe.osigner.infra.output.port.SignPort;
import org.oefa.gob.pe.osigner.util.FileUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class IText7Adapter implements SignPort {

    private final String OSIGNER_DIRECTORY = System.getProperty("user.home") + AppConfiguration.getKey("OSIGNER_FOLDER");
    private final String SIGNED_FOLDER = AppConfiguration.getKey("SIGNED_FOLDER");

    @Override
    public void signFilesFromSignConfiguration(SignConfiguration signConfiguration, CertificateModel certificate) throws Exception {
        ITSAClient tsa = null;
        int index = 1;

        if(signConfiguration.getSignProcessConfiguration().isTimeStamp()) {
            tsa = new TSAClientBouncyCastle(
                    signConfiguration.getSignProcessConfiguration().getUrlTsa(),
                    signConfiguration.getSignProcessConfiguration().getUserTsa(),
                    signConfiguration.getSignProcessConfiguration().getPassTsa(),
                    Constant.TSA_TOKEN_SIZE,
                    Constant.HASH
            );
        }

        for(FileModel fileToSign : signConfiguration.getFilesToSign()){
            if(fileToSign.getPage() != Constant.FIRMA_PAGINA_ERROR) {

                String pathOut = FileUtil.getSignedFolder() + fileToSign.getName();
                String pathIn = fileToSign.getLocation() + fileToSign.getName();
                FileOutputStream fout = new FileOutputStream(pathOut);
                PdfReader reader = new PdfReader(new File(pathIn));
                PdfSigner signer = new PdfSigner(reader, fout, new StampingProperties().useAppendMode());
                PdfSignatureAppearance sap = signer.getSignatureAppearance();

                if (signConfiguration.getSignProcessConfiguration().getSignatureStyle() != 0) {
                    if (signConfiguration.getSignProcessConfiguration().getSignatureStyle() == 1) {
                        sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
                    } else {
                        sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);
                        sap.setSignatureGraphic(ImageDataFactory.create(signConfiguration.getSignProcessConfiguration().getSignatureImage()));
                    }

                    sap.setLayer2Text(
                            buildSignatureText(signConfiguration.getSignProcessConfiguration(), certificate.getNombre())
                    );
                    sap.setLayer2FontSize(fileToSign.getFontSize());

                    if(signConfiguration.getSignProcessConfiguration().getSignatureType()== Constant.FIRMA_TIPO_FIRMA && signConfiguration.getSignProcessConfiguration().getSignaturePositionType() == Constant.FIRMA_POSICION_AUTOMATICA)
                        sap.setImage(ImageDataFactory.create(ResourceFX.loadResource("FirmaFondoBlanco.png").readAllBytes()));

                    sap.setPageRect(new Rectangle(
                            fileToSign.getPositionX(),
                            fileToSign.getPositionY(),
                            fileToSign.getWidth(),
                            fileToSign.getHeight()
                    )).setPageNumber(fileToSign.getPage());
                }
                sap.setReason(Constant.VERIFICA_PREFIJO + " - " + signConfiguration.getSignProcessConfiguration().getReason());
                sap.setLocation(signConfiguration.getSignProcessConfiguration().getLocation());

                PrivateKeySignature pks = new PrivateKeySignature(
                        certificate.getKey(),
                        Constant.HASH_FORMAT_2,
                        Constant.PROVIDER
                );

                signer.signDetached(
                        new BouncyCastleDigest(),
                        pks, certificate.getChain(),
                        null,
                        null,
                        tsa,
                        Constant.SIGNATURE_SIZE,
                        PdfSigner.CryptoStandard.CADES
                );

                fileToSign.setLocation(FileUtil.getSignedFolder());
                fileToSign.setEstadoOperacion(Constant.FIRMA_ESTADO_FIRMADO);

                reader.close();
                fout.flush();
                fout.close();

                FileUtil.deleteFile(pathIn);
            }

            double progress = (double) index/signConfiguration.getFilesToSign().size();
            if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN))
                NotificationFX.updateProgressNotification(
                        Constant.PROGRESS_VALUE_SIGN.getInitialValue(),
                        Constant.PROGRESS_VALUE_SIGN.getPartialValue() * progress);

            index++;
        }

    }

    private SignConfiguration addLTV(ArrayList<FileModel> fileList) throws Exception{
        return null;
    }

    private String buildSignatureText(SignProcessModel signProcessModel, String userName){
        String signatureType = signProcessModel.getSignatureType() == Constant.FIRMA_TIPO_FIRMA ? "Firmado" : "Visado";
        String signatureText = signatureType + " digitalmente por: " + userName + "\n";
        if(signProcessModel.getUserRole() != null && !signProcessModel.getUserRole().equals(""))
            signatureText += "Cargo: " + signProcessModel.getUserRole() + "\n";

        if(signProcessModel.getLocation() != null && !signProcessModel.getLocation().equals("") && signProcessModel.getSignatureType() == Constant.FIRMA_TIPO_FIRMA)
            signatureText += "Lugar: " + signProcessModel.getLocation() + "\n";

        if(signProcessModel.getReason() != null && !signProcessModel.getReason().equals(""))
            signatureText += "Motivo: " + signProcessModel.getReason() + "\n";

        if(signProcessModel.getSignatureOptionalText() != null && !signProcessModel.getSignatureOptionalText().equals(""))
            signatureText += signProcessModel.getSignatureOptionalText() + "\n";

        signatureText += "Fecha/Hora: " + signProcessModel.getFechaCreacion();

        return signatureText;

    }


}
