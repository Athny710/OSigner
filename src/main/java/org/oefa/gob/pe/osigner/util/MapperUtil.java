package org.oefa.gob.pe.osigner.util;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.domain.FileModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.domain.SignProcessModel;
import org.oefa.gob.pe.osigner.domain.ws.DocProcesoFirma;
import org.oefa.gob.pe.osigner.domain.ws.GrupoProcesoFirma;
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
                cargo,
                locacion,
                razon,
                proceso,
                usuarioId,
                dni,
                tipoFirma,
                signatureStyle,
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
                    ssResponse.getPage().get(i),
                    ssResponse.getFilesBytes().get(i)
            );
            fileModelList.add(fileToSign);
        }

        return new SignConfiguration(signProcessModel, fileModelList);

    }

    public static SignConfiguration mapMassiveSignatureResponseToSignConfiguration(){
        return null;
    }
}
