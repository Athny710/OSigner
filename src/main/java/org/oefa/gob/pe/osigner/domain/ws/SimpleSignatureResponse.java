package org.oefa.gob.pe.osigner.domain.ws;

import java.util.ArrayList;

public class SimpleSignatureResponse {

    private boolean visible;
    private boolean ltv;
    private boolean selloTiempo;
    private String fechaCreacion;
    private String selloTiempoUrl;
    private String selloTiempoUser;
    private String selloTiempoPass;
    private String cargo;
    private String locacion;
    private String razon;
    private String proceso;
    private String usuarioId;
    private String dni;
    private Integer tipoFirma;
    private Integer estiloFirma;
    private byte[] imagenFirma;
    private ArrayList<Float> x;
    private ArrayList<Float> y;
    private ArrayList<Float> width;
    private ArrayList<Float> height;
    private ArrayList<Integer> page;
    private ArrayList<String> filesName;
    private ArrayList<byte[]> filesBytes;


    public SimpleSignatureResponse(boolean visible, boolean ltv, boolean selloTiempo, String fechaCreacion, String selloTiempoUrl, String selloTiempoUser, String selloTiempoPass, String cargo, String locacion, String razon, String proceso, String usuarioId, String dni, Integer tipoFirma, Integer estiloFirma, ArrayList<byte[]> filesBytes,ArrayList<Float> x, ArrayList<Float> y, ArrayList<Float> width, ArrayList<Float> height, ArrayList<Integer> page, ArrayList<String> filesName) {
        this.visible = visible;
        this.ltv = ltv;
        this.selloTiempo = selloTiempo;
        this.fechaCreacion = fechaCreacion;
        this.selloTiempoUrl = selloTiempoUrl;
        this.selloTiempoUser = selloTiempoUser;
        this.selloTiempoPass = selloTiempoPass;
        this.cargo = cargo;
        this.locacion = locacion;
        this.razon = razon;
        this.proceso = proceso;
        this.usuarioId = usuarioId;
        this.dni = dni;
        this.tipoFirma = tipoFirma;
        this.estiloFirma = estiloFirma;
        this.filesBytes = filesBytes;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.page = page;
        this.filesName = filesName;
    }

    public Integer getEstiloFirma() {
        return estiloFirma;
    }

    public void setEstiloFirma(Integer estiloFirma) {
        this.estiloFirma = estiloFirma;
    }

    public Integer getTipoFirma() {
        return tipoFirma;
    }

    public void setTipoFirma(Integer tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLtv() {
        return ltv;
    }

    public void setLtv(boolean ltv) {
        this.ltv = ltv;
    }

    public boolean isSelloTiempo() {
        return selloTiempo;
    }

    public void setSelloTiempo(boolean selloTiempo) {
        this.selloTiempo = selloTiempo;
    }

    public String getSelloTiempoUrl() {
        return selloTiempoUrl;
    }

    public void setSelloTiempoUrl(String selloTiempoUrl) {
        this.selloTiempoUrl = selloTiempoUrl;
    }

    public String getSelloTiempoUser() {
        return selloTiempoUser;
    }

    public void setSelloTiempoUser(String selloTiempoUser) {
        this.selloTiempoUser = selloTiempoUser;
    }

    public String getSelloTiempoPass() {
        return selloTiempoPass;
    }

    public void setSelloTiempoPass(String selloTiempoPass) {
        this.selloTiempoPass = selloTiempoPass;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getLocacion() {
        return locacion;
    }

    public void setLocacion(String locacion) {
        this.locacion = locacion;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public byte[] getImagenFirma() {
        return imagenFirma;
    }

    public void setImagenFirma(byte[] imagenFirma) {
        this.imagenFirma = imagenFirma;
    }

    public ArrayList<Float> getX() {
        return x;
    }

    public void setX(ArrayList<Float> x) {
        this.x = x;
    }

    public ArrayList<Float> getY() {
        return y;
    }

    public void setY(ArrayList<Float> y) {
        this.y = y;
    }

    public ArrayList<Float> getWidth() {
        return width;
    }

    public void setWidth(ArrayList<Float> width) {
        this.width = width;
    }

    public ArrayList<Float> getHeight() {
        return height;
    }

    public void setHeight(ArrayList<Float> height) {
        this.height = height;
    }

    public ArrayList<Integer> getPage() {
        return page;
    }

    public void setPage(ArrayList<Integer> page) {
        this.page = page;
    }

    public ArrayList<String> getFilesName() {
        return filesName;
    }

    public void setFilesName(ArrayList<String> filesName) {
        this.filesName = filesName;
    }

    public ArrayList<byte[]> getFilesBytes() {
        return filesBytes;
    }

    public void setFilesBytes(ArrayList<byte[]> filesBytes) {
        this.filesBytes = filesBytes;
    }
}
