package org.oefa.gob.pe.osigner.infra.output.adapter;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.util.encoders.Base64;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class WSSFDAdapter implements RestPort {


    private final String URL_SERVER = AppConfiguration.getKey("SSFD_URL_SERVER");
    private final RestTemplate REST_TEMPLATE = createRestTemplate();
    private final Gson GSON = new Gson();


    private RestTemplate createRestTemplate(){
        SimpleClientHttpRequestFactory rf = new SimpleClientHttpRequestFactory();
        rf.setBufferRequestBody(false);
        return new RestTemplate(rf);
    }


    /**
     * Función que permite obtener la configuración del proceso de firma masiva.
     * @return Objeto que contiene toda la información necesaria para el proceso de firma.
     * @throws Exception Excepción conectándose al servicio
     */
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


    /**
     * Función que permite obtener la configuración del proceso de firma simple.
     * @return Objeto que contiene toda la información necesaria para el proceso de firma.
     * @throws Exception Excepción al conectarse al servicio.
     */
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


    /**
     * Función que permite completar/terminar el proceso de firma simple.
     * @param grupoFirmado Objeto que contiene los archivos firmados que se subirán al servidor.
     * @throws Exception Excepción al conectase al servicio.
     */
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


    /**
     * Función que permite actualizar el estado del Grupo de Operación.
     * @param status Estado al que se actualizará el Grupo de Operación.
     * @throws Exception Excepción al conectarse al servicio.
     */
    @Override
    public void updateSignProcess(int status) throws Exception {
        String url = URL_SERVER + "actualizarGrupoOperacion/" + AppConfiguration.ID_GROUP + "/" + status;

        ResponseEntity<String> response = REST_TEMPLATE.getForEntity(url,String.class);
        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error conectando al servicio: " + url);
        }

    }


    /**
     * Función que permite completar el proceso de firma masiva.
     * @param signConfiguration Objeto que contiene la configuración de firma.
     * @param zipFile Archivo zip que contiene los archivos firmados.
     * @throws Exception Excepción al conectarse a alguno de los servicios.
     */
    @Override
    public void completeMassiveSignProcess(SignConfiguration signConfiguration, File zipFile) throws Exception {
        uploadSignedFilesRest(signConfiguration, zipFile);
        updateSignedFilesRest(signConfiguration);
        completeProcessWSSFD(signConfiguration.getFilesToSign());

    }


    /**
     * Función que permite cancelar el proceso de firma simple.
     * @throws Exception Excepción al conectarse al servicio.
     */
    @Override
    public void cancelSimpleSignProccess() throws Exception {
        cancelProccessWSSFD();

    }


    /**
     * Función que permite cancelar el proceso de firma masiva.
     * @param signConfiguration Objeto que contiene la configuración de firma.
     * @throws Exception Excepción al conectarse al servicio de firma.
     */
    @Override
    public void cancelMassiveSignProccess(SignConfiguration signConfiguration) throws Exception {
        cancelProccessRest(signConfiguration);
        cancelProccessWSSFD();

    }


    /**
     * Función que permite subir los archivos firmados a la plataforma que inició el proceso de firma.
     * @param signConfiguration Configuración de firma.
     * @param zipFile Archivo .zip que contiene los archivos firmados.
     * @throws Exception Excepción al conectarse al servicio.
     */
    private void uploadSignedFilesRest(SignConfiguration signConfiguration, File zipFile) throws Exception{
        File fileToUpload = new File(zipFile.getPath());
        String uploadUrl = signConfiguration.getSignProcessConfiguration().getUploadRestService();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            AtomicLong transferredBytes = new AtomicLong();
            long contentLength = fileToUpload.length();

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addPart("file", new FileBody(fileToUpload, ContentType.DEFAULT_BINARY) {
                        @Override
                        public void writeTo(OutputStream out) throws IOException {
                            try (InputStream inputStream = new FileInputStream(fileToUpload)) {
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                    transferredBytes.addAndGet(bytesRead);

                                    double progress = (double) transferredBytes.get() / contentLength;
                                    NotificationFX.updateProgressNotification(
                                            Constant.PROGRESS_VALUE_UPLOAD.getInitialValue(),
                                            Constant.PROGRESS_VALUE_UPLOAD.getPartialValue() * progress
                                    );
                                }
                            }
                        }
                    });

            org.apache.http.HttpEntity entity = entityBuilder.build();
            HttpPost post = new HttpPost(uploadUrl);
            post.setEntity(entity);
            post.setHeader("Authorization", SignConfiguration.getInstance().getSignProcessConfiguration().getAuthorizationTokenService());

            HttpResponse response = httpClient.execute(post);

        } catch (IOException e) {
            throw e;
        }
    }


    /**
     * Función que permite actualizar la información de los archivos que fueron enviados en la plataforma que inició el proceso de firma.
     * @param signConfiguration Configuración de firma.
     * @throws Exception Excepción al conectarse al servicio.
     */
    private void updateSignedFilesRest(SignConfiguration signConfiguration) throws Exception{
        String url = signConfiguration.getSignProcessConfiguration().getUpdateRestService();
        FirmaMasivaFinalizadaRequest body = MapperUtil.mapSignConfigurationToFirmaFinalizadaRequest(signConfiguration);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", signConfiguration.getSignProcessConfiguration().getAuthorizationTokenService());
        headers.add("Content-Type", "application/json");

        HttpEntity<FirmaMasivaFinalizadaRequest> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.POST, request, String.class);

        if(!response.getStatusCode().is2xxSuccessful())
            throw new Exception("Error conectando al servicio: " + url);


    }


    /**
     * Función que completa el proceso de firma masiva en el servicio de firma WSSFDFirmador.
     * Se encarga de actualizar el estado del Grupo de Operación y de los archivos.
     * @param signedFiles Lista de archivos firmados.
     * @throws Exception Excepción conectándose al servicio.
     */
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


    /**
     * Función que cancela el proceso de firma en el servicio de firma WSSFDFirmador
     * @throws Exception Excepción al conectarse al servicio.
     */
    private void cancelProccessWSSFD() throws Exception {
        String url = URL_SERVER + "cancelarGrupoProcesoFirma/" + AppConfiguration.ID_GROUP;

        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.PUT, null, String.class);
        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error conectando al servicio: " + url);
        }

    }


    /**
     * Función que cancela el proceso de firma en el servicio que está consumiendo el servicio de firma.
     * Sirve para poder eliminar el archivo .zip que se generó al iniciar el proceso de firma.
     * @param signConfiguration Configuración de firma que contiene la URL del servicio.
     * @throws Exception Excepción al conectarse al servicio.
     */
    private void cancelProccessRest(SignConfiguration signConfiguration) throws Exception {
        String url = signConfiguration.getSignProcessConfiguration().getCancelRestService();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", signConfiguration.getSignProcessConfiguration().getAuthorizationTokenService());

        HttpEntity<FirmaMasivaFinalizadaRequest> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.GET, request,String.class);

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error conectando al servicio: " + url);
        }

    }


    /**
     * Función utilizada por el proceso de firma simple que permite obtener la imagen de firma.
     * @param processId ID del proceso de firma.
     * @param userId ID del usuario.
     * @param signatureType Tipo de firma (firma o visado).
     * @return Bytes de la imagen.
     * @throws Exception Excepción al conectarse al servicio.
     */
    private byte[] getSignatureImage(String processId,  String userId, Integer signatureType) throws Exception{
        String url = URL_SERVER + "devolverImage/" + processId + "/" + userId + "/" + signatureType;

        ResponseEntity<String> response =  REST_TEMPLATE.getForEntity(url, String.class);
        if(!response.getStatusCode().is2xxSuccessful())
            throw new Exception("Error conectando al servicio: " + url);

        SignatureImage signatureImage = GSON.fromJson(response.getBody(), SignatureImage.class);

        return signatureImage.getImagen();

    }


    /**
     * Función que permite desencryptar la contraseña del servicio de sello de tiempo que se configura en el servicio WSSFDFirmador.
     * @param encryptedPassword Contraseña encryptada.
     * @return Contraseña desencryptada y lista para utilziarse.
     * @throws Exception Excepción al desencryptar la contraseña.
     */
    private String decryptTsaPassword(String encryptedPassword) throws Exception{
        if (encryptedPassword == null || encryptedPassword.length() == 0)
            return "";

        Key key = new SecretKeySpec(Constant.TSA_PASS_HASH_KEY, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(2, key);
        byte[] decodedValue = c.doFinal(Base64.decode(encryptedPassword));

        return new String(decodedValue);

    }


    /**
     * Función utilizada por el proceso de firma simple; ya que este obtiene la infomación de firma de un archivo properties.
     * @param propertiesBytes Bytes del archivo properties.
     * @return Objeto con las propiedades del archivo properties.
     * @throws Exception Excepción al leer los bytes.
     */
    private Properties loadPropertiesFromByte(byte[] propertiesBytes) throws Exception{
        Properties properties = new Properties();
        InputStream input = new ByteArrayInputStream(propertiesBytes);
        properties.load(input);

        return properties;

    }

}
