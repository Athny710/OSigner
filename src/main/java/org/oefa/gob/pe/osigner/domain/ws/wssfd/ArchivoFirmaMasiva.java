package org.oefa.gob.pe.osigner.domain.ws.wssfd;

public class ArchivoFirmaMasiva {

    private Long id;
    private int idArchivo;
    private String name;
    private int codigoOperacion;
    private String claveVerificacion;
    private String fechaCreacion;
    private String fechaActualizacion;
    private int estadoOperacion; //0,1,2,4,5

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

    public String getClaveVerificacion() {
        return claveVerificacion;
    }

    public void setClaveVerificacion(String claveVerificacion) {
        this.claveVerificacion = claveVerificacion;
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

    public int getEstadoOperacion() {
        return estadoOperacion;
    }

    public void setEstadoOperacion(int estadoOperacion) {
        this.estadoOperacion = estadoOperacion;
    }

}
