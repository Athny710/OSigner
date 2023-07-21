package org.oefa.gob.pe.osigner.application;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.infra.output.adapter.WSSFDAdapter;

import java.util.ArrayList;

public class RestService {

    private static final WSSFDAdapter WSSFD_ADAPTER = new WSSFDAdapter();

    public static void getSignConfiguration() throws Exception{
        if(AppConfiguration.APP_TYPE.equals(AppType.SIMPLE_SIGN)){
            SignConfiguration.createInstace(WSSFD_ADAPTER.getSimpleSignConfiguration());
        }else {
            SignConfiguration.createInstace(WSSFD_ADAPTER.getMassiveSignConfiguration());
        }

    }

    public static void uploadFilesSigned(SignConfiguration signConfiguration) throws Exception{
        if(AppConfiguration.APP_TYPE.equals(AppType.SIMPLE_SIGN)){
            WSSFD_ADAPTER.uploadFilesSimpleSign(signConfiguration.getFilesToSign());
        }else{
            WSSFD_ADAPTER.uploadFilesMassiveSign(signConfiguration);
        }

    }

    public static void cancelSignProcess() throws Exception{
        WSSFD_ADAPTER.cancelSignProccess();

    }


}
