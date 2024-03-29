package org.oefa.gob.pe.osigner.util;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.domain.SignProcessModel;
import org.oefa.gob.pe.osigner.domain.ws.ArchivoFirmado;
import org.oefa.gob.pe.osigner.domain.ws.MassiveSignatureResponse;
import org.oefa.gob.pe.osigner.domain.ws.rest.FirmaMasivaFinalizadaRequest;
import org.oefa.gob.pe.osigner.domain.ws.wssfd.ArchivoFirmaMasiva;
import org.oefa.gob.pe.osigner.domain.ws.wssfd.DocProcesoFirma;
import org.oefa.gob.pe.osigner.domain.ws.wssfd.GrupoProcesoFirma;
import org.oefa.gob.pe.osigner.domain.ws.SimpleSignatureResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MapperUtil {


    public static SimpleSignatureResponse mapPropertiesToSimpleSignatureResponse(Properties p, GrupoProcesoFirma gpf){
        boolean visible = p.getProperty("firmavisible").equals("true");
        boolean selloTiempo = p.getProperty("sellotiempo").equals("1");
        boolean ltv = p.getProperty("tipo.duracion").equals("1") && selloTiempo;
        String selloTiempoUrl = selloTiempo ? p.getProperty("urlTsa") : "";
        String selloTiempoUser = selloTiempo ? p.getProperty("usuariotsa") : "";
        String selloTiempoPass = selloTiempo ? p.getProperty("clavetsa") : "";
        String locacion = p.getProperty("locacion");
        String cargo = p.getProperty("cargo");
        String razon = p.getProperty("motivo");
        String proceso = p.getProperty("proceso_id");
        String usuarioId = p.getProperty("usuario_id");
        String dni = p.getProperty("DNI");
        Integer fontSize = Integer.parseInt(p.getProperty("tamanio"));
        Integer tipoFirma = Integer.parseInt(p.getProperty("firmado.visado"));
        List<Float> listX = Arrays.stream(p.getProperty("x").split(Pattern.quote(">"))).map(Float::parseFloat).toList();
        List<Float> listY = Arrays.stream(p.getProperty("y").split(Pattern.quote(">"))).map(Float::parseFloat).toList();
        List<Float> listHeight = Arrays.stream(p.getProperty("alto").split(Pattern.quote(">"))).map(Float::parseFloat).toList();
        List<Float> listWidth = Arrays.stream(p.getProperty("ancho").split(Pattern.quote(">"))).map(Float::parseFloat).toList();
        List<Integer> listPage = Arrays.stream(p.getProperty("pagina").split(Pattern.quote(">"))).map(Integer::parseInt).toList();
        List<String> listNames = Arrays.stream(p.getProperty("nombrePdf").split(Pattern.quote(">"))).toList();

        AppConfiguration.DNI_CLIENT = dni;
        int signatureStyle = visible ? 1 : 0;
        ArrayList<byte[]> filesBytes = gpf.getListDocs().stream().map(DocProcesoFirma::getBytes).collect(Collectors.toCollection(ArrayList::new));
        return new SimpleSignatureResponse(
                visible,
                ltv,
                selloTiempo,
                gpf.getFechaRegistro(),
                selloTiempoUrl,
                selloTiempoUser,
                selloTiempoPass,
                StringUtil.formatString(cargo),
                StringUtil.formatString(locacion),
                StringUtil.formatString(razon),
                proceso,
                usuarioId,
                dni,
                tipoFirma,
                signatureStyle,
                fontSize,
                filesBytes,
                new ArrayList<>(listX),
                new ArrayList<>(listY),
                new ArrayList<>(listWidth),
                new ArrayList<>(listHeight),
                new ArrayList<>(listPage),
                new ArrayList<>(listNames)
        );
    }

    public static SignConfiguration mapSimpleSignatureResponseToSignConfiguration(SimpleSignatureResponse ssResponse){

        SignProcessModel signProcessModel = new SignProcessModel(
           ssResponse.getDni(),
           ssResponse.getCargo(),
           ssResponse.getLocacion(),
           ssResponse.getRazon(),
           ssResponse.getFechaCreacion(),
           ssResponse.isSelloTiempo(),
           false,
           ssResponse.getSelloTiempoUrl(),
           ssResponse.getSelloTiempoUser(),
           ssResponse.getSelloTiempoPass(),
           ssResponse.getTipoFirma(),
           ssResponse.getEstiloFirma() == 1 && ssResponse.getImagenFirma() != null ? 2 : ssResponse.getEstiloFirma(),
           ssResponse.getImagenFirma()
        );

        List<FileModel> fileModelList = new ArrayList<>();
        for(int i=0; i < ssResponse.getFilesName().size(); i++){
            FileModel fileToSign = new FileModel(
                    ssResponse.getFilesName().get(i),
                    ssResponse.getX().get(i),
                    ssResponse.getY().get(i),
                    ssResponse.getHeight().get(i),
                    ssResponse.getWidth().get(i),
                    ssResponse.getTamanio(),
                    ssResponse.getPage().get(i),
                    ssResponse.getFilesBytes().get(i)
            );
            fileModelList.add(fileToSign);
        }

        return new SignConfiguration(signProcessModel, fileModelList);

    }

    public static SignConfiguration mapMassiveSignatureResponseToSignConfiguration(MassiveSignatureResponse msResponse){
        SignProcessModel signProcessModel = new SignProcessModel();
        signProcessModel.setId(msResponse.getConfiguracionFirma().getId());
        signProcessModel.setClientUUID(msResponse.getConfiguracionFirma().getClientUUID());
        signProcessModel.setUsernameSSFD(msResponse.getConfiguracionFirma().getUsernameSSFD());
        signProcessModel.setUserDNI(msResponse.getConfiguracionFirma().getUserDNI());
        signProcessModel.setUserRole(msResponse.getConfiguracionFirma().getUserRole());
        signProcessModel.setLocation(msResponse.getConfiguracionFirma().getLocation());
        signProcessModel.setReason(msResponse.getConfiguracionFirma().getReason());
        signProcessModel.setSignatureOptionalText(msResponse.getConfiguracionFirma().getSignatureOptionalText());
        signProcessModel.setOptional(msResponse.getConfiguracionFirma().getOptional());
        signProcessModel.setFechaCreacion(msResponse.getConfiguracionFirma().getFechaCreacion());
        signProcessModel.setZipUUID(msResponse.getConfiguracionFirma().getZipUUID());

        signProcessModel.setDownloadRestService(msResponse.getConfiguracionFirma().getDownloadRestService());
        signProcessModel.setUploadRestService(msResponse.getConfiguracionFirma().getUploadRestService());
        signProcessModel.setUpdateRestService(msResponse.getConfiguracionFirma().getUpdateRestService());
        signProcessModel.setCancelRestService(msResponse.getConfiguracionFirma().getCancelRestService());
        signProcessModel.setAuthorizationTokenService(msResponse.getConfiguracionFirma().getAuthorizationTokenService());

        signProcessModel.setUrlTsa(msResponse.getConfiguracionFirma().getServicioFirma().getUrl());
        signProcessModel.setUserTsa(msResponse.getConfiguracionFirma().getServicioFirma().getUsuario());
        signProcessModel.setPassTsa(msResponse.getConfiguracionFirma().getServicioFirma().getClave());

        signProcessModel.setTimeStamp(msResponse.getConfiguracionFirma().getSignatureLevel().equals("STSA"));
        signProcessModel.setLtv(msResponse.getConfiguracionFirma().getSignatureLevel().equals("SLTV"));

        signProcessModel.setSignatureType(msResponse.getConfiguracionFirma().getSignatureType());
        signProcessModel.setSignatureStyle(msResponse.getConfiguracionFirma().getSignatureStyle());
        signProcessModel.setSignaturePositionType(msResponse.getConfiguracionFirma().getSignaturePositionType());
        signProcessModel.setSignatureRelativePosition(msResponse.getConfiguracionFirma().getSignatureRelativePosition());

        signProcessModel.setGlosaVerificacion(msResponse.getConfiguracionFirma().getGlosaVerificacion());
        signProcessModel.setGlosaText(msResponse.getConfiguracionFirma().getExtension().getTexto());
        signProcessModel.setGlosaUrl(msResponse.getConfiguracionFirma().getExtension().getUrl());

        signProcessModel.setSignatureImage(msResponse.getImagenFirma());

        List<FileModel> fileModelList = new ArrayList<>();
        for(ArchivoFirmaMasiva archivo : msResponse.getListaArchivos()){
            FileModel fileToSign = new FileModel(
                    archivo.getId(),
                    archivo.getIdArchivo(),
                    archivo.getName(),
                    archivo.getCodigoOperacion(),
                    archivo.getEstadoOperacion(),
                    archivo.getClaveVerificacion(),
                    msResponse.getConfiguracionFirma().getPositionX(),
                    msResponse.getConfiguracionFirma().getPositionY(),
                    msResponse.getConfiguracionFirma().getHeight(),
                    msResponse.getConfiguracionFirma().getWidth(),
                    msResponse.getConfiguracionFirma().getFontSize(),
                    msResponse.getConfiguracionFirma().getPage(),
                    archivo.getGlosa() == 1
            );
            fileModelList.add(fileToSign);
        }

        return new SignConfiguration(signProcessModel, fileModelList);

    }

    public static List<ArchivoFirmado> mapSignConfigurationToSignedFilesList(List<FileModel> signedFiles){
        List<ArchivoFirmado> archivosFirmadoRequest = new ArrayList<>();

        for(FileModel archivo : signedFiles){
            ArchivoFirmado request = new ArchivoFirmado();
            request.setId(archivo.getId().intValue());
            request.setIdArchivo(archivo.getIdArchivo());
            request.setNombreArchivo(archivo.getName());
            request.setCodigoOperacion(String.valueOf(archivo.getCodigoOperacion()));
            request.setCodigoVerificacion(archivo.getClaveVerificacion());
            request.setEstadoOperacion(archivo.getEstadoOperacion());

            archivosFirmadoRequest.add(request);
        }

        return archivosFirmadoRequest;
    }

    public static FirmaMasivaFinalizadaRequest mapSignConfigurationToFirmaFinalizadaRequest(SignConfiguration signConfiguration){
        List<ArchivoFirmado> archivosFirmadosList = new ArrayList<>();
        for(FileModel fileSigned : signConfiguration.getFilesToSign()){
            ArchivoFirmado archivoFirmado = new ArchivoFirmado();
            archivoFirmado.setId(fileSigned.getId().intValue());
            archivoFirmado.setIdArchivo(fileSigned.getIdArchivo());
            archivoFirmado.setNombreArchivo(fileSigned.getName());
            archivoFirmado.setCodigoOperacion(String.valueOf(fileSigned.getCodigoOperacion()));
            archivoFirmado.setCodigoVerificacion(fileSigned.getClaveVerificacion());
            archivoFirmado.setEstadoOperacion(fileSigned.getEstadoOperacion());

            archivosFirmadosList.add(archivoFirmado);
        }

        FirmaMasivaFinalizadaRequest firmaMasivaFinalizadaRequest = new FirmaMasivaFinalizadaRequest();
        firmaMasivaFinalizadaRequest.setGrupoOperacion(Integer.valueOf(AppConfiguration.ID_GROUP));
        firmaMasivaFinalizadaRequest.setClientUUID(signConfiguration.getSignProcessConfiguration().getClientUUID());
        firmaMasivaFinalizadaRequest.setOptional(signConfiguration.getSignProcessConfiguration().getOptional());
        firmaMasivaFinalizadaRequest.setArchivos(archivosFirmadosList);

        return firmaMasivaFinalizadaRequest;

    }
}
