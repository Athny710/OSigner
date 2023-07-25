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
    private String zipName;

    private String urlTsa;
    private String userTsa;
    private String passTsa;

    private boolean timeStamp;
    private boolean ltv;

    private int signatureType;
    private int signatureStyle;
    private int glosaVerificacion;
    private int signaturePositionType;
    private int signatureRelativePosition;

    private byte[] signatureImage;
    private String glosaText;
    private String glosaUrl;


    public SignProcessModel(){

    }

    public SignProcessModel(String userDNI, String userRole, String location, String reason, String fechaCreacion, boolean timeStamp, boolean ltv, String urlTsa, String userTsa, String passTsa, int signatureType, int signatureStyle, byte[] singatureImage){
        this.userDNI = userDNI;
        this.userRole = userRole;
        this.location = location;
        this.reason = reason;
        this.fechaCreacion = fechaCreacion;
        this.timeStamp = timeStamp;
        this.ltv = ltv;
        this.urlTsa = urlTsa;
        this.userTsa = userTsa;
        this.passTsa = passTsa;
        this.signatureType = signatureType;
        this.signatureStyle = signatureStyle;
        this.signatureImage = singatureImage;

    }

    public boolean isTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(boolean timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isLtv() {
        return ltv;
    }

    public void setLtv(boolean ltv) {
        this.ltv = ltv;
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

    public String getUserDNI() {
        return userDNI;
    }

    public void setUserDNI(String userDNI) {
        this.userDNI = userDNI;
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

    public int getSignatureRelativePosition() {
        return signatureRelativePosition;
    }

    public void setSignatureRelativePosition(int signatureRelativePosition) {
        this.signatureRelativePosition = signatureRelativePosition;
    }

    public byte[] getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(byte[] signatureImage) {
        this.signatureImage = signatureImage;
    }

    public String getGlosaText() {
        return glosaText;
    }

    public void setGlosaText(String glosaText) {
        this.glosaText = glosaText;
    }

    public String getGlosaUrl() {
        return glosaUrl;
    }

    public void setGlosaUrl(String glosaUrl) {
        this.glosaUrl = glosaUrl;
    }

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName;
    }

    public int getGlosaVerificacion() {
        return glosaVerificacion;
    }

    public void setGlosaVerificacion(int glosaVerificacion) {
        this.glosaVerificacion = glosaVerificacion;
    }
}
