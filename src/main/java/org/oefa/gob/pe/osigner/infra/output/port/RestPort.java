package org.oefa.gob.pe.osigner.infra.output.port;

import org.oefa.gob.pe.osigner.application.SignService;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;

import java.util.ArrayList;
import java.util.List;

public interface RestPort {

    SignConfiguration getSimpleSignConfiguration() throws Exception;

    SignConfiguration getMassiveSignConfiguration() throws Exception;

    void uploadFilesSimpleSign(List<FileModel> fileList) throws Exception;

    void uploadFilesMassiveSign(SignConfiguration signConfiguration) throws Exception;

    void cancelSignProccess() throws Exception;

}
