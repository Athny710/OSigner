package org.oefa.gob.pe.osigner.infra.output.port;

import org.oefa.gob.pe.osigner.application.SignService;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.domain.ws.wssfd.GrupoFirmado;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface RestPort {

    SignConfiguration getSimpleSignConfiguration() throws Exception;

    SignConfiguration getMassiveSignConfiguration() throws Exception;

    void completeSimpleSignProcess(GrupoFirmado grupoFirmado) throws Exception;

    void completeMassiveSignProcess(SignConfiguration signConfiguration, File zipFile) throws Exception;

    void cancelSignProccess() throws Exception;

    void updateSignProcess(int status) throws Exception;

}
