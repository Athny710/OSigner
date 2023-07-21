package org.oefa.gob.pe.osigner.domain;

public class FileModel {

    private Long id;
    private int idArchivo;
    private String name;

    private int codigoOperacion;
    private int estadoOperacion;
    private String claveVerificacion;

    private String location;
    private byte[] bytes;

    private Float positionX;
    private Float positionY;
    private Float height;
    private Float width;
    private int page;

    public FileModel(String name, Float positionX, Float positionY, Float height, Float width, int page, byte[] bytes){
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.height = height;
        this.width = width;
        this.page = page;
        this.bytes = bytes;
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

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCodigoOperacion() {
        return codigoOperacion;
    }

    public void setCodigoOperacion(int codigoOperacion) {
        this.codigoOperacion = codigoOperacion;
    }

    public int getEstadoOperacion() {
        return estadoOperacion;
    }

    public void setEstadoOperacion(int estadoOperacion) {
        this.estadoOperacion = estadoOperacion;
    }

    public String getClaveVerificacion() {
        return claveVerificacion;
    }

    public void setClaveVerificacion(String claveVerificacion) {
        this.claveVerificacion = claveVerificacion;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
