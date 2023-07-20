package org.oefa.gob.pe.osigner.infra.output.port;

import org.oefa.gob.pe.osigner.domain.SignConfiguration;

public interface SignPort {

    SignConfiguration signFiles(SignConfiguration signConfiguration) throws Exception;
}
