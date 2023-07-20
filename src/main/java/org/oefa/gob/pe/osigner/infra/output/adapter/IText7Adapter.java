package org.oefa.gob.pe.osigner.infra.output.adapter;

import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.infra.output.port.SignPort;

import java.util.ArrayList;

public class IText7Adapter implements SignPort {

    @Override
    public SignConfiguration signFiles(SignConfiguration signConfiguration) throws Exception {
        /*
        ArrayList<FileModel> fileList = new ArrayList<>();

        ITSAClient tsa = null;
        int index = 0;

        if(this.signConfigurationModel.isSelloTiempo())
            tsa = new TSAClientBouncyCastle(
                    signConfigurationModel.getSelloTiempoUrl(),
                    signConfigurationModel.getSelloTiempoUser(),
                    signConfigurationModel.getSelloTiempoPass(),
                    Constant.TSA_TOKEN_SIZE,
                    Constant.HASH
            );

        for(byte[] fileBytes : this.signConfigurationModel.getFilesBytes()) {
            FileModel fileSigned = new FileModel();
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();

                PdfReader reader = new PdfReader(bis);
                PdfSigner signer = new PdfSigner(reader, bout, new StampingProperties().useAppendMode());
                PdfSignatureAppearance sap = signer.getSignatureAppearance();

                if (signConfigurationModel.isVisible()) {
                    if (signConfigurationModel.getImagenFirma() == null) {
                        sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
                    } else {
                        sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);
                        sap.setSignatureGraphic(ImageDataFactory.create(signConfigurationModel.getImagenFirma()));
                    }
                    sap.setLayer2Text(signConfigurationModel.getTexto().replace("#", certificate.getNombre()));
                    sap.setReason(signConfigurationModel.getRazon());
                    sap.setLocation(signConfigurationModel.getLocacion());
                    sap.setPageRect(new Rectangle(
                            signConfigurationModel.getX().get(index),
                            signConfigurationModel.getY().get(index),
                            signConfigurationModel.getWidth().get(index),
                            signConfigurationModel.getHeight().get(index)
                    )).setPageNumber(signConfigurationModel.getPage().get(index));
                }

                PrivateKeySignature pks = new PrivateKeySignature(
                        certificate.getKey(),
                        ApplicationConstants.HASH_FORMAT_2,
                        ApplicationConstants.PROVIDER
                );

                signer.signDetached(
                        new BouncyCastleDigest(),
                        pks, certificate.getChain(),
                        null,
                        null,
                        tsa,
                        ApplicationConstants.SIGNATURE_SIZE,
                        PdfSigner.CryptoStandard.CADES
                );

                fileSigned.setName(signConfigurationModel.getFilesName().get(index));
                fileSigned.setBytes(bout.toByteArray());

                fileList.add(fileSigned);

                index++;
                reader.close();
                bout.flush();
                bout.close();
            }catch (Exception e ){
                System.out.println(ExceptionUtils.getStackTrace(e));
            }

        }

        return fileList;

         */
        return null;
    }

    private SignConfiguration addLTV(ArrayList<FileModel> fileList) throws Exception{
        return null;
    }
}
