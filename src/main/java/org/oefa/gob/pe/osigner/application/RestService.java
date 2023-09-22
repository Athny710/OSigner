package org.oefa.gob.pe.osigner.application;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.domain.ws.wssfd.GrupoFirmado;
import org.oefa.gob.pe.osigner.infra.output.adapter.WSSFDAdapter;
import org.oefa.gob.pe.osigner.util.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RestService {


    private static final WSSFDAdapter WSSFD_ADAPTER = new WSSFDAdapter();


    /**
     * Función que se encarga de obtener la información de firma para poder iniciar con el proceso de firma.
     * @throws Exception Excepción al momento de conectarse al servicio y obtener la información.
     */
    public static void getSignConfiguration() throws Exception{
        if(AppConfiguration.APP_TYPE.equals(AppType.SIMPLE_SIGN)){
            SignConfiguration.createInstace(WSSFD_ADAPTER.getSimpleSignConfiguration());

        }else {
            SignConfiguration.createInstace(WSSFD_ADAPTER.getMassiveSignConfiguration());

        }

    }


    /**
     * Función que se encarga de completar el proceso de firma y subir los archivos firmados.
     * @param signConfiguration Configuración de firma.
     * @throws Exception Excepción al momento de conectarse al servicio y subir los archivos firmados.
     */
    public static void uploadFilesSigned(SignConfiguration signConfiguration) throws Exception{
        if(AppConfiguration.APP_TYPE.equals(AppType.SIMPLE_SIGN)){
            FileUtil.loadFilesBytes(signConfiguration.getFilesToSign());
            ArrayList<byte[]> fileSignedBytes = signConfiguration.getFilesToSign()
                    .stream()
                    .map(FileModel::getBytes)
                    .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<String> fileSignedNames = signConfiguration.getFilesToSign()
                    .stream()
                    .map(FileModel::getName)
                    .collect(Collectors.toCollection(ArrayList::new));
            GrupoFirmado grupoFirmado = new GrupoFirmado(
                    Integer.parseInt(AppConfiguration.ID_GROUP),
                    fileSignedNames,
                    fileSignedBytes
            );

            WSSFD_ADAPTER.completeSimpleSignProcess(grupoFirmado);

        }else{
            File zipFile = new File(FileUtil.getSignedFolder() + signConfiguration.getSignProcessConfiguration().getZipUUID() + ".zip");
            WSSFD_ADAPTER.completeMassiveSignProcess(signConfiguration, zipFile);

        }

    }


    /**
     * Función que se encarga de actualizar el proceso de firma (Grupo de operación) en el servicio de firma.
     * @param status 4: Firma completada - 5: Archivos firmados subidos y actualizados.
     * @throws Exception Excepción al momento de consumir el servicio.
     */
    public static void updateSignProcess(int status) throws Exception{
        WSSFD_ADAPTER.updateSignProcess(status);

    }


    /**
     * Función que se encarga de cancelar el proceso de firma.
     * @throws Exception Excepción al momento de consumir el servicio.
     */
    public static void cancelSignProcess(SignConfiguration signConfiguration) throws Exception{
        if(AppConfiguration.APP_TYPE.equals(AppType.SIMPLE_SIGN)){
            WSSFD_ADAPTER.cancelSimpleSignProccess();
        }else{
            WSSFD_ADAPTER.cancelMassiveSignProccess(signConfiguration);
        }

    }


}
