package org.oefa.gob.pe.osigner.application;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.infra.output.adapter.WSSFDAdapter;

import java.util.ArrayList;

public class RestService {

    private static final WSSFDAdapter WSSFD_ADAPTER = new WSSFDAdapter();

    public static SignConfiguration getSignConfigurationModel() throws Exception{
        SignConfiguration signConfiguration;

        if(AppConfiguration.APP_TYPE.equals(AppType.SIMPLE_SIGN)){

        }
        return WSSFD_ADAPTER.getSignConfiguration();

    }

    public static void uploadFilesSigned(ArrayList<FileModel> signedFiles) throws Exception{
        WSSFD_ADAPTER.uploadFilesSigned(signedFiles);

    }

    public static void cancelSignProcess() throws Exception{
        WSSFD_ADAPTER.cancelSignProccess();

    }


}
