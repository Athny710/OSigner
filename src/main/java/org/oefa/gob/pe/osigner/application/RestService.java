package org.oefa.gob.pe.osigner.application;

import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfigurationModel;
import org.oefa.gob.pe.osigner.infra.output.adapter.WSSFDAdapter;

import java.util.ArrayList;

public class RestService {

    private static final WSSFDAdapter WSSFD_ADAPTER = new WSSFDAdapter();

    public static SignConfigurationModel getSignConfigurationModel() throws Exception{
        return WSSFD_ADAPTER.getSignConfiguration();

    }

    public static void uploadFilesSigned(ArrayList<FileModel> signedFiles) throws Exception{
        WSSFD_ADAPTER.uploadFilesSigned(signedFiles);

    }

    public static void cancelSignProcess() throws Exception{
        WSSFD_ADAPTER.cancelSignProccess();

    }


}
