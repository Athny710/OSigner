package org.oefa.gob.pe.osigner.infra.output.port;

import org.oefa.gob.pe.osigner.domain.SignConfigurationModel;

public interface SignPort {

    SignConfigurationModel signFiles(SignConfigurationModel signConfigurationModel) throws Exception;
}
