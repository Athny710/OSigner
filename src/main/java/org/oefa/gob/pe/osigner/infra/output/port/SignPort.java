package org.oefa.gob.pe.osigner.infra.output.port;

import org.oefa.gob.pe.osigner.domain.CertificateModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;

public interface SignPort {

    SignConfiguration signFilesFromSignConfiguration(SignConfiguration signConfiguration, CertificateModel certificateModel) throws Exception;
}
