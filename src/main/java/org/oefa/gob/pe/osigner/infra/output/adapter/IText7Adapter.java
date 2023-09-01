package org.oefa.gob.pe.osigner.infra.output.adapter;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.*;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.LoaderFX;
import org.oefa.gob.pe.osigner.core.NotificationFX;
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
            String pathOut = OSIGNER_DIRECTORY + SIGNED_FOLDER + fileToSign.getName();
            String pathIn = fileToSign.getLocation() + fileToSign.getName();
            FileOutputStream fout = new FileOutputStream(pathOut);
            PdfReader reader = new PdfReader(new File(pathIn));
            PdfSigner signer = new PdfSigner(reader, fout, new StampingProperties().useAppendMode());
            PdfSignatureAppearance sap = signer.getSignatureAppearance();

            if (signConfiguration.getSignProcessConfiguration().getSignatureStyle() != 0){
                if (signConfiguration.getSignProcessConfiguration().getSignatureStyle() == 1) {
                    sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
                } else {
                    sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);
                    sap.setSignatureGraphic(ImageDataFactory.create(signConfiguration.getSignProcessConfiguration().getSignatureImage()));
                }

                sap.setLayer2Text(
                        buildSignatureText(signConfiguration.getSignProcessConfiguration(), certificate.getNombre())
                );
                sap.setImage(ImageDataFactory.create(LoaderFX.loadResource("FirmaFondoBlanco.png").readAllBytes()));
                sap.setPageRect(new Rectangle(
                        fileToSign.getPositionX(),
                        fileToSign.getPositionY(),
                        fileToSign.getWidth(),
                        fileToSign.getHeight()
                )).setPageNumber(fileToSign.getPage());
            }
            sap.setReason(signConfiguration.getSignProcessConfiguration().getReason());
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

            fileToSign.setLocation(pathOut);
            fileToSign.setEstadoOperacion(Constant.FIRMA_ESTADO_FIRMADO);

            reader.close();
            fout.flush();
            fout.close();

            FileUtil.deleteFile(pathIn);
            double progress = (double) index/signConfiguration.getFilesToSign().size();

            if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN))
                NotificationFX.updateProgressNotification(
                        Constant.PROGRESS_VALUE_SIGN.getInitialValue(),
                        Constant.PROGRESS_VALUE_SIGN.getPartialValue() * progress);
        }

    }

    private SignConfiguration addLTV(ArrayList<FileModel> fileList) throws Exception{
        return null;
    }

    private String buildSignatureText(SignProcessModel signProcessModel, String userName){
        String signatureText = "Firmado digitalmente por: " + userName + "\n";
        if(!signProcessModel.getUserRole().equals(""))
            signatureText += "Cargo: " + signProcessModel.getUserRole() + "\n";

        if(!signProcessModel.getLocation().equals(""))
            signatureText += "Lugar: " + signProcessModel.getLocation() + "\n";

        if(!signProcessModel.getReason().equals(""))
            signatureText += "Motivo: " + signProcessModel.getReason() + "\n";

        signatureText += "Fecha/Hora: " + signProcessModel.getFechaCreacion();

        return signatureText;

    }


}
