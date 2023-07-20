package org.oefa.gob.pe.osigner.domain;

public class SignProcessModel {

    private Long id;
    private String clientUUID;
    private String usernameSSFD;
    private String userDNI;
    private String userRole;
    private String location;
    private String reason;
    private String fechaCreacion;

    private String downloadRestService;
    private String uploadRestService;
    private String authorizationTokenService;

    private String urlTsa;
    private String userTsa;
    private String passTsa;
    private String proceso;

    private int signatureType;
    private int signatureStyle;
    private int signaturePositionType;
    private int signatureRelativePosition;
    /*
    private int positionX;
    private int positionY;
    private int page;
    private int fontSize;
    private int height;
    private int width;

     */

    private byte[] signatureImage;
    private String glosaText;


    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getUserDNI() {
        return userDNI;
    }

    public void setUserDNI(String userDNI) {
        this.userDNI = userDNI;
    }

    public String getGlosaText() {
        return glosaText;
    }

    public void setGlosaText(String glosaText) {
        this.glosaText = glosaText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientUUID() {
        return clientUUID;
    }

    public void setClientUUID(String clientUUID) {
        this.clientUUID = clientUUID;
    }

    public String getUsernameSSFD() {
        return usernameSSFD;
    }

    public void setUsernameSSFD(String usernameSSFD) {
        this.usernameSSFD = usernameSSFD;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDownloadRestService() {
        return downloadRestService;
    }

    public void setDownloadRestService(String downloadRestService) {
        this.downloadRestService = downloadRestService;
    }

    public String getUploadRestService() {
        return uploadRestService;
    }

    public void setUploadRestService(String uploadRestService) {
        this.uploadRestService = uploadRestService;
    }

    public String getAuthorizationTokenService() {
        return authorizationTokenService;
    }

    public void setAuthorizationTokenService(String authorizationTokenService) {
        this.authorizationTokenService = authorizationTokenService;
    }

    public String getUrlTsa() {
        return urlTsa;
    }

    public void setUrlTsa(String urlTsa) {
        this.urlTsa = urlTsa;
    }

    public String getUserTsa() {
        return userTsa;
    }

    public void setUserTsa(String userTsa) {
        this.userTsa = userTsa;
    }

    public String getPassTsa() {
        return passTsa;
    }

    public void setPassTsa(String passTsa) {
        this.passTsa = passTsa;
    }

    public int getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(int signatureType) {
        this.signatureType = signatureType;
    }

    public int getSignatureStyle() {
        return signatureStyle;
    }

    public void setSignatureStyle(int signatureStyle) {
        this.signatureStyle = signatureStyle;
    }

    public int getSignaturePositionType() {
        return signaturePositionType;
    }

    public void setSignaturePositionType(int signaturePositionType) {
        this.signaturePositionType = signaturePositionType;
    }

    public byte[] getSignatureImage() {
        return signatureImage;
    }

    public int getSignatureRelativePosition() {
        return signatureRelativePosition;
    }


    public void setSignatureRelativePosition(int signatureRelativePosition) {
        this.signatureRelativePosition = signatureRelativePosition;
    }

    public void setSignatureImage(byte[] signatureImage) {
        this.signatureImage = signatureImage;
    }
}
