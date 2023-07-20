package org.oefa.gob.pe.osigner.infra.output.port;

import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;

import java.util.ArrayList;

public interface RestPort {

    SignConfiguration getSimpleSignConfiguration() throws Exception;

    void uploadFilesSigned(ArrayList<FileModel> fileList) throws Exception;

    void cancelSignProccess() throws Exception;

}
