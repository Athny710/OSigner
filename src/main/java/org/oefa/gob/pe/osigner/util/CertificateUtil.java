package org.oefa.gob.pe.osigner.util;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.domain.CertificateModel;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;

public class CertificateUtil {


    public static List<CertificateModel> certificateList = null;


    /**
     * Función que se encarga de cargar los certificados.
     * @throws Exception Excepción al momento de cargar los certificados.
     */
    public static void loadCertificates() throws Exception{
        certificateList = getCertificates();

    }


    /**
     * Función que se encarga de volver a obtener los certificados válidos en el sistema operativo.
     * @param userDNI DNI del usuario con el que se filtrarán los certificados encontrados.
     * @return Lista de certificados encontrados.
     * @throws Exception Excepción al momento de cargar los certificados.
     */
    public static ArrayList<String> reloadCertificateList(String userDNI) throws Exception{
        return getCertificates()
                .stream()
                .filter(cert -> cert.getNumeroDocumento().equals(userDNI))
                .map(CertificateModel::getAlias)
                .collect(Collectors.toCollection(ArrayList::new));

    }


    /**
     * Función que se encarga de obtener los certificados para un determinado usuario.
     * @param userDNI DNI del usuario con el que se filtrarán los certificados encontrados.
     * @return Lista de certificados encontrados.
     */
    public static ArrayList<String> getUserCertificateList(String userDNI){
        return certificateList
                .stream()
                .filter(cert -> cert.getNumeroDocumento().equals(userDNI))
                .map(CertificateModel::getAlias)
                .collect(Collectors.toCollection(ArrayList::new));

    }


    /**
     * Función que obtiene parámetros de firma del certificado para realizar la firma digital.
     * @param alias Alias del certificado.
     * @return Clase con las propiedades del certificado.
     * @throws Exception Excepción al momento de cargar la información de firma del certificado.
     */
    public static CertificateModel getCertificateToSignByAlias(String alias) throws Exception{
        CertificateModel cert = certificateList
                .stream()
                .filter(c -> c.getAlias().equals(alias))
                .findFirst()
                .orElseThrow();


        Security.addProvider(new BouncyCastleProvider());
        KeyStore ks = KeyStore.getInstance(Constant.KEY_STORE);
        ks.load(null, null);

        PrivateKey key = (PrivateKey) ks.getKey(alias, null);
        java.security.cert.Certificate[] chain = ks.getCertificateChain(alias);

        if(key == null || chain == null)
            throw new Exception("Error al obtener la key o chain del certificado");

        cert.setChain(chain);
        cert.setKey(key);

        return cert;
    }


    /**
     * Función que obtiene todos los certificados válidos del sistema operativo.
     * @return Lista de certificados válidos encontrados.
     * @throws Exception Exceptión al momento de cargar la información de los certificados.
     */
    private static List<CertificateModel> getCertificates() throws Exception {
        if (!Constant.OS.contains("win"))
            return Collections.emptyList();

        Optional<KeyStore> ksOpt = loadKeyStore(Constant.KEY_STORE);
        if(ksOpt.isEmpty())
            return Collections.emptyList();

        return buildAndGetCertificates(ksOpt.get());
    }


    private static List<CertificateModel> buildAndGetCertificates(KeyStore keyStore) throws Exception {
        List<CertificateModel> list = new ArrayList<>();
        X509CRL crl = loadServerCrl();
        Enumeration<String> certificatesListFromKs = keyStore.aliases();
        while(certificatesListFromKs.hasMoreElements()){
            Optional<CertificateModel> certificate = buildAndValidCertificate(certificatesListFromKs.nextElement(), keyStore, crl);
            if(certificate.isPresent()){
                if(StringUtil.isCertificateValidToSign(certificate.get().getAlias()))
                    list.add(certificate.get());

            }
        }

        return list;

    }


    private static Optional<CertificateModel> buildAndValidCertificate(String alias, KeyStore keyStore, X509CRL crl) throws Exception {

        X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
        if(!isCertificateValid(x509Certificate, crl))
            return Optional.empty();

        try {
            String nombre = getCertUserName(x509Certificate);
            String numeroDocumento = getSerialNumber(x509Certificate);
            String creationDate = Constant.DATE_FORMAT.format(x509Certificate.getNotBefore().getTime());
            String expirationDate = Constant.DATE_FORMAT.format(x509Certificate.getNotAfter().getTime());
            CertificateModel certificate = new CertificateModel(alias, nombre, creationDate, expirationDate, numeroDocumento);

            return Optional.of(certificate);
        }catch (Exception e){
            return Optional.empty();
        }

    }


    private static Optional<KeyStore> loadKeyStore(String instance) throws Exception{
        KeyStore ks = KeyStore.getInstance(instance);
        ks.load(null);
        return Optional.of(ks);

    }


    /**
     * Función que se encarga de validar el certificado.
     * @param certificate Certificado que se desea validar.
     * @param crl Archivo CRL con el cual se va a validar el certificado.
     * @return True si el certificado es válido.
     */
    private static boolean isCertificateValid(X509Certificate certificate, X509CRL crl){
        try {
            certificate.checkValidity();
            if (crl.isRevoked(certificate))
                throw new Exception("Certificado revocado");

            return true;

        }catch (Exception e){
            return false;
        }

    }


    /**
     * Función que se encarga de obtener el nombre de la persona del certificado.
     * @param certificate Certificado del que se desea obtener la información.
     * @return Nombre de la persona.
     * @throws Exception Excepción al momento de obtener la información.
     */
    private static String getCertUserName(X509Certificate certificate) throws Exception {
        String dn = certificate.getSubjectDN().toString();
        LdapName ln = new LdapName(dn);
        Rdn rdnName = ln.getRdns().stream().filter(r -> r.getType().equalsIgnoreCase("GIVENNAME")).findFirst().orElseThrow();
        Rdn rdnLastName = ln.getRdns().stream().filter(r -> r.getType().equalsIgnoreCase("SURNAME")).findFirst().orElseThrow();

        return rdnName.getValue().toString() + " " + rdnLastName.getValue().toString();

    }


    /**
     * Función que se encarga de obtener el número de documento del certificados. Por lo general es el DNI.
     * @param certificate Certificados del que se desea obtener la información.
     * @return Número de documento
     * @throws Exception Excepción al momento de obtener la información.
     */
    private static String getSerialNumber(X509Certificate certificate) throws Exception {
        String dn = certificate.getSubjectDN().toString();
        String[] tagserie = new String[]{"SERIALNUMBER"};

        LdapName ln = new LdapName(dn);
        Rdn rdn = ln.getRdns().stream().filter(r -> r.getType().equalsIgnoreCase(tagserie[0])).findFirst().orElseThrow();
        String value = rdn.getValue().toString();

        String separator = value.contains("DNI") ? ":" : "-";

        return value.split(separator)[1];

    }


    /**
     * Función que se encarga de descargar el CRL del servidor mediante el cual se validarán los certificados.
     * @return CRL.
     */
    private static X509CRL loadServerCrl() {
        try {
            String crlFileName = "X509CRL.crl";
            FileUtil.saveFileFromUrl(AppConfiguration.getKey("CRL_URL_SERVER"), crlFileName);
            File crlFile = new File(FileUtil.getTempFolder() + crlFileName);
            InputStream is = new FileInputStream(crlFile);

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            return (X509CRL) certificateFactory.generateCRL(is);

        }catch (Exception e){
            LogUtil.setError(
                    "Error obteniendo crl desde: ",
                    CertificateUtil.class.getName(),
                    e
            );
            return null;
        }
    }

}
