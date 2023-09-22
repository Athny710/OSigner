package org.oefa.gob.pe.osigner.domain.ws.wssfd;

public class ConfiguracionFirmaMasiva {

    private Long id;
    private String clientUUID;
    private String userDNI;
    private String usernameSSFD;
    private String userRole;
    private String location;
    private String reason;
    private String signatureOptionalText;
    private String optional;

    private String downloadRestService;
    private String uploadRestService;
    private String updateRestService;
    private String cancelRestService;
    private String authorizationTokenService;

    private int signatureType; // 1,2
    private int signatureStyle; // 0,1,2
    private String signatureLevel; // SB, STSA, SLTV
    private int glosaVerificacion;
    private int signaturePositionType; //1, 2, 3
    private int signatureRelativePosition; // 1,2,3,4,5,6,7,8,9
    private Float positionX;
    private Float positionY;
    private int page;
    private int fontSize;
    private Float width;
    private Float height;

    private String fechaCreacion;
    private String fechaActualizacion;
    private String zipUUID;

    private GrupoProcesoFirma grupoProcesoFirma;
    private ServicioFirma servicioFirma;
    private Extension extension;


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

    public String getUserDNI() {
        return userDNI;
    }

    public void setUserDNI(String userDNI) {
        this.userDNI = userDNI;
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

    public String getSignatureOptionalText() {
        return signatureOptionalText;
    }

    public void setSignatureOptionalText(String signatureOptionalText) {
        this.signatureOptionalText = signatureOptionalText;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
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

    public String getUpdateRestService() {
        return updateRestService;
    }

    public void setUpdateRestService(String updateRestService) {
        this.updateRestService = updateRestService;
    }

    public String getCancelRestService() {
        return cancelRestService;
    }

    public void setCancelRestService(String cancelRestService) {
        this.cancelRestService = cancelRestService;
    }

    public String getAuthorizationTokenService() {
        return authorizationTokenService;
    }

    public void setAuthorizationTokenService(String authorizationTokenService) {
        this.authorizationTokenService = authorizationTokenService;
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

    public String getSignatureLevel() {
        return signatureLevel;
    }

    public void setSignatureLevel(String signatureLevel) {
        this.signatureLevel = signatureLevel;
    }

    public int getGlosaVerificacion() {
        return glosaVerificacion;
    }

    public void setGlosaVerificacion(int glosaVerificacion) {
        this.glosaVerificacion = glosaVerificacion;
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

    public Float getPositionX() {
        return positionX;
    }

    public void setPositionX(Float positionX) {
        this.positionX = positionX;
    }

    public Float getPositionY() {
        return positionY;
    }

    public void setPositionY(Float positionY) {
        this.positionY = positionY;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getZipUUID() {
        return zipUUID;
    }

    public void setZipUUID(String zipUUID) {
        this.zipUUID = zipUUID;
    }

    public GrupoProcesoFirma getGrupoProcesoFirma() {
        return grupoProcesoFirma;
    }

    public void setGrupoProcesoFirma(GrupoProcesoFirma grupoProcesoFirma) {
        this.grupoProcesoFirma = grupoProcesoFirma;
    }

    public ServicioFirma getServicioFirma() {
        return servicioFirma;
    }

    public void setServicioFirma(ServicioFirma servicioFirma) {
        this.servicioFirma = servicioFirma;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }
}
