package org.oefa.gob.pe.osigner.infra.output.adapter;

import com.google.gson.Gson;
import org.apache.http.entity.mime.content.FileBody;
import org.bouncycastle.util.encoders.Base64;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.domain.ws.ArchivoFirmado;
import org.oefa.gob.pe.osigner.domain.ws.MassiveSignatureResponse;
import org.oefa.gob.pe.osigner.domain.ws.SimpleSignatureResponse;
import org.oefa.gob.pe.osigner.domain.ws.rest.FirmaMasivaFinalizadaRequest;
import org.oefa.gob.pe.osigner.domain.ws.wssfd.*;
import org.oefa.gob.pe.osigner.infra.output.port.RestPort;
import org.oefa.gob.pe.osigner.util.MapperUtil;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.util.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class WSSFDAdapter implements RestPort {


    private final String URL_SERVER = AppConfiguration.getKey("SSFD_URL_SERVER");
    private final RestTemplate REST_TEMPLATE = new RestTemplate();
    private final Gson GSON = new Gson();


    @Override
    public SignConfiguration getMassiveSignConfiguration() throws Exception{
        String url = URL_SERVER + "obtenerConfiguracionFirmaMasiva/" + AppConfiguration.ID_GROUP;

        ResponseEntity<String>  response = REST_TEMPLATE.getForEntity(url, String.class);
        if(!response.getStatusCode().is2xxSuccessful())
            throw new Exception("Error conectando al servicio: " + url);

        ResponseConfiguracionMasiva responseWSEntity = GSON.fromJson(response.getBody(), ResponseConfiguracionMasiva.class);
        if(!responseWSEntity.isStatus())
            throw new Exception(responseWSEntity.getMensaje());

        MassiveSignatureResponse massiveSignatureResponse = responseWSEntity.getResponseFirmaMasiva();
        massiveSignatureResponse.getConfiguracionFirma().getServicioFirma().setClave(
                this.decryptTsaPassword(massiveSignatureResponse.getConfiguracionFirma().getServicioFirma().getClave())
        );

        return MapperUtil.mapMassiveSignatureResponseToSignConfiguration(massiveSignatureResponse);

    }


    @Override
    public SignConfiguration getSimpleSignConfiguration() throws Exception {
        String url = URL_SERVER + "verificarNuevoGrupoFirmado/" + AppConfiguration.ID_CLIENT;

        ResponseEntity<String> response =  REST_TEMPLATE.getForEntity(url, String.class);
        if(!response.getStatusCode().is2xxSuccessful())
            throw new Exception("Error conectando al servicio: " + url);


        ResponseProcesoFirma responseWSEntity =  GSON.fromJson(response.getBody(), ResponseProcesoFirma.class);
        if(!responseWSEntity.isStatus())
            throw new Exception(responseWSEntity.getMessage());

        GrupoProcesoFirma gpf = responseWSEntity.getGpf();
        SimpleSignatureResponse simpleSignatureResponse = MapperUtil.mapPropertiesToSimpleSignatureResponse(
                this.loadPropertiesFromByte(gpf.getBytesConf()),
                gpf
        );
        simpleSignatureResponse.setImagenFirma(
                this.getSignatureImage(simpleSignatureResponse.getProceso(), simpleSignatureResponse.getUsuarioId(), simpleSignatureResponse.getTipoFirma())
        );
        simpleSignatureResponse.setSelloTiempoPass(
                this.decryptTsaPassword(simpleSignatureResponse.getSelloTiempoPass())
        );

        return MapperUtil.mapSimpleSignatureResponseToSignConfiguration(simpleSignatureResponse);

    }


    @Override
    public void completeSimpleSignProcess(GrupoFirmado grupoFirmado) throws Exception {
        String url = URL_SERVER + "uploadGrupoFirmado/";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");

        HttpEntity<GrupoFirmado> requestEntity = new HttpEntity<>(grupoFirmado, headers);
        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        if(!response.getStatusCode().is2xxSuccessful())
            throw new Exception("Error conectando al servicio: " + url);

    }

    @Override
    public void cancelSignProccess() throws Exception {
        String url = URL_SERVER + "cancelarGrupoProcesoFirma/" + AppConfiguration.ID_GROUP;

        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.PUT, null, String.class);
        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error conectando al servicio: " + url);
        }

    }


    @Override
    public void updateSignProcess(int status) throws Exception {
        String url = URL_SERVER + "actualizarGrupoOperacion/" + AppConfiguration.ID_GROUP + "/" + status;

        ResponseEntity<String> response = REST_TEMPLATE.getForEntity(url,String.class);
        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error conectando al servicio: " + url);
        }

    }

    @Override
    public void completeMassiveSignProcess(SignConfiguration signConfiguration, File zipFile) throws Exception {
        uploadSignedFiles(signConfiguration, zipFile);
        updateSignedFiles(signConfiguration);
        completeProcessWSSFD(signConfiguration.getFilesToSign());
    }


    private void uploadSignedFiles(SignConfiguration signConfiguration, File zipFile) throws Exception{


        /*
        String url = signConfiguration.getSignProcessConfiguration().getUploadRestService();
        Resource resource = new FileSystemResource(zipFile.getPath());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", signConfiguration.getSignProcessConfiguration().getAuthorizationTokenService());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", resource);

        MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(multipartBody, headers);

        ResponseEntity<String> response = REST_TEMPLATE.postForEntity(url, request, String.class);

        if(!response.getStatusCode().is2xxSuccessful())
            throw new Exception("Error conectando al servicio: " + url);

         */

    }

    private void updateSignedFiles(SignConfiguration signConfiguration) throws Exception{
        String url = signConfiguration.getSignProcessConfiguration().getUpdateRestService();
        FirmaMasivaFinalizadaRequest body = MapperUtil.mapSignConfigurationToFirmaFinalizadaRequest(signConfiguration);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", signConfiguration.getSignProcessConfiguration().getAuthorizationTokenService());
        headers.add("Content-Type", "application/json");

        HttpEntity<FirmaMasivaFinalizadaRequest> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = REST_TEMPLATE.postForEntity(url, request, String.class);

        if(!response.getStatusCode().is2xxSuccessful())
            throw new Exception("Error conectando al servicio: " + url);


    }


    private void completeProcessWSSFD(List<FileModel> signedFiles) throws Exception{
        String url = URL_SERVER + "finalizarProcesoFirmaMasiva/" + AppConfiguration.ID_GROUP;
        List<ArchivoFirmado> signedFilesList = MapperUtil.mapSignConfigurationToSignedFilesList(signedFiles);
        FirmaFinalizadaRequest body = new FirmaFinalizadaRequest(signedFilesList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");

        HttpEntity<FirmaFinalizadaRequest> request = new HttpEntity<>(body,headers);
        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.POST, request, String.class);

        if(!response.getStatusCode().is2xxSuccessful())
            throw new Exception("Error conectando al servicio: " + url);


    }



    private byte[] getSignatureImage(String processId,  String userId, Integer signatureType) throws Exception{
        String url = URL_SERVER + "devolverImage/" + processId + "/" + userId + "/" + signatureType;

        ResponseEntity<String> response =  REST_TEMPLATE.getForEntity(url, String.class);
        if(!response.getStatusCode().is2xxSuccessful())
            throw new Exception("Error conectando al servicio: " + url);

        SignatureImage signatureImage = GSON.fromJson(response.getBody(), SignatureImage.class);

        return signatureImage.getImagen();

    }


    private String decryptTsaPassword(String encryptedPassword) throws Exception{
        if (encryptedPassword == null || encryptedPassword.length() == 0)
            return "";

        Key key = new SecretKeySpec(Constant.TSA_PASS_HASH_KEY, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(2, key);
        byte[] decodedValue = c.doFinal(Base64.decode(encryptedPassword));

        return new String(decodedValue);

    }


    private Properties loadPropertiesFromByte(byte[] propertiesBytes) throws Exception{
        Properties properties = new Properties();
        InputStream input = new ByteArrayInputStream(propertiesBytes);
        properties.load(input);

        return properties;

    }
}
