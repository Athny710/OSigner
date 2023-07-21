package org.oefa.gob.pe.osigner.application;

import org.oefa.gob.pe.osigner.domain.CertificateModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.infra.output.adapter.IText7Adapter;

public class SignService {

    private static final IText7Adapter ITEXT7_ADAPTER= new IText7Adapter();

    public static void signFiles(CertificateModel certificateModel) throws Exception {
        SignConfiguration.updateInstance(
                ITEXT7_ADAPTER.signFilesFromSignConfiguration(
                        SignConfiguration.getInstance(),
                        certificateModel
                ).getFilesToSign()
        );

    }
}
