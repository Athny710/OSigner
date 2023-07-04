package org.oefa.gob.pe.osigner.infra.output.adapter;

import com.google.gson.Gson;
import org.bouncycastle.util.encoders.Base64;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfigurationModel;
import org.oefa.gob.pe.osigner.domain.ws.ResponseProcesoFirma;
import org.oefa.gob.pe.osigner.infra.output.port.RestPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.ArrayList;

public class WSSFDAdapter implements RestPort {

    private final String URL_SERVER = AppConfiguration.getKey("SSFD_URL_SERVER");
    private final RestTemplate REST_TEMPLATE = new RestTemplate();
    private final Gson GSON = new Gson();


    @Override
    public SignConfigurationModel getSignConfiguration() throws Exception {
        String url = URL_SERVER + "verificarNuevoGrupoFirmado/" + AppConfiguration.ID_CLIENT;

        ResponseEntity<String> response =  REST_TEMPLATE.getForEntity(url, String.class);
        if(!response.getStatusCode().is2xxSuccessful()){
            throw new Exception("Error conectando al servicio: " + url);
        }

        ResponseProcesoFirma responseWSEntity =  GSON.fromJson(response.getBody(), ResponseProcesoFirma.class);
        if(!responseWSEntity.isStatus()) {
            throw new Exception(responseWSEntity.getMessage());
        }

        SignConfigurationModel signConfigurationModel = responseWSEntity.getSignConfigurationModel();
        signConfigurationModel.setSelloTiempoPass(
                decryptTsaPassword(signConfigurationModel.getSelloTiempoPass())
        );

        return signConfigurationModel;

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

    private String decryptTsaPassword(String encryptedPassword) throws Exception{
        if (encryptedPassword == null || encryptedPassword.length() == 0)
            return "";

        Key key = new SecretKeySpec(Constant.TSA_PASS_HASH_KEY, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(2, key);
        byte[] decodedValue = c.doFinal(Base64.decode(encryptedPassword));

        return new String(decodedValue);

    }
}
