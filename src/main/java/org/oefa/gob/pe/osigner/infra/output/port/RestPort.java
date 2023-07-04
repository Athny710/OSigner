package org.oefa.gob.pe.osigner.infra.output.port;

import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfigurationModel;

import java.util.ArrayList;

public interface RestPort {

    SignConfigurationModel getSignConfiguration() throws Exception;

    void uploadFilesSigned(ArrayList<FileModel> fileList) throws Exception;

    void cancelSignProccess() throws Exception;

}
