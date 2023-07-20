package org.oefa.gob.pe.osigner.infra.output.adapter;

import com.google.gson.Gson;
import org.bouncycastle.util.encoders.Base64;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.domain.SignProcessModel;
import org.oefa.gob.pe.osigner.domain.ws.GrupoProcesoFirma;
import org.oefa.gob.pe.osigner.domain.ws.ResponseProcesoFirma;
import org.oefa.gob.pe.osigner.infra.output.port.RestPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WSSFDAdapter implements RestPort {

    private final String URL_SERVER = AppConfiguration.getKey("SSFD_URL_SERVER");
    private final RestTemplate REST_TEMPLATE = new RestTemplate();
    private final Gson GSON = new Gson();


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

        SignConfiguration configuration = this.getSignConfigurationFromProperties(gpf.getBytesConf(), gpf.getFechaRegistro());





        return signConfiguration;

    }

    @Override
    public void uploadFilesSigned(ArrayList<FileModel> fileList) throws Exception {

    }

    @Override
    public void cancelSignProccess() throws Exception {
        String url = URL_SERVER + "cancelarGrupoProcesoFirma/" + AppConfiguration.ID_GROUP;

        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.PUT, null, String.class);
        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error conectando al servicio: " + url);
        }

    }

    private SignConfiguration getSignConfigurationFromProperties(byte[] confBytes, String fechaRegistro) throws Exception{
        Properties p = this.loadUserConfigurationProperties(confBytes);

        SignProcessModel signProcess = new SignProcessModel();
        signProcess.setClientUUID(AppConfiguration.ID_CLIENT);
        signProcess.setUserRole(p.getProperty("cargo"));
        signProcess.setUserDNI(p.getProperty("DNI"));
        signProcess.setLocation(p.getProperty("locacion"));
        signProcess.setReason(p.getProperty("motivo"));
        signProcess.setFechaCreacion(fechaRegistro);
        signProcess.setUrlTsa(
                p.getProperty("sellotiempo").equals("1") ? p.getProperty("urlTsa") : ""
        );
        signProcess.setUserTsa(
                p.getProperty("sellotiempo").equals("1") ? p.getProperty("usuariotsa") : ""
        );
        signProcess.setPassTsa(
                decryptTsaPassword(
                    p.getProperty("sellotiempo").equals("1") ? p.getProperty("clavetsa") : ""
                )
        );
        signProcess.setProceso(p.getProperty("proceso_id"));
        signProcess.setSignatureType(Integer.parseInt(p.getProperty("firmado.visado")));

        if(p.getProperty("firmavisible").equals("true")){
            if(p.getProperty("firmado.visado").equals("1")){
                signProcess.setSignatureStyle(2);
            }else{
                signProcess.setSignatureStyle(1);
            }
        }else{
            signProcess.setSignatureStyle(0);
        }

        List<FileModel> fileList = new ArrayList<>();
        List<Float> listX = Arrays.stream(p.getProperty("x").split(Pattern.quote(">"))).map(Float::parseFloat).toList();
        List<Float> listY = Arrays.stream(p.getProperty("y").split(Pattern.quote(">"))).map(Float::parseFloat).toList();
        List<Float> listHeight = Arrays.stream(p.getProperty("alto").split(Pattern.quote(">"))).map(Float::parseFloat).toList();
        List<Float> listWidth = Arrays.stream(p.getProperty("ancho").split(Pattern.quote(">"))).map(Float::parseFloat).toList();
        List<Integer> listPage = Arrays.stream(p.getProperty("pagina").split(Pattern.quote(">"))).map(Integer::parseInt).toList();
        List<String> listNames = Arrays.stream(p.getProperty("nombrePdf").split(Pattern.quote(">"))).toList();
        for(int i=0; i < listNames.size(); i++){
            FileModel fileModel = new FileModel(
                    listNames.get(i),
                    listX.get(i),
                    listY.get(i),
                    listHeight.get(i),
                    listWidth.get(i),
                    listPage.get(i)
            );
            fileList.add(fileModel);
        }

        return new SignConfiguration(signProcess, fileList);
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


    private Properties loadUserConfigurationProperties(byte[] propertiesBytes) throws Exception{
        Properties properties = new Properties();
        InputStream input = new ByteArrayInputStream(propertiesBytes);
        properties.load(input);

        return properties;

    }
}
