package org.oefa.gob.pe.osigner.domain.ws;

public class ArchivoFirmado {

    private Integer id;
    private Integer idArchivo;
    private String nombreArchivo;
    private String codigoVerificacion;
    private String codigoOperacion;
    private Integer estadoOperacion;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public String getCodigoOperacion() {
        return codigoOperacion;
    }

    public void setCodigoOperacion(String codigoOperacion) {
        this.codigoOperacion = codigoOperacion;
    }

    public Integer getEstadoOperacion() {
        return estadoOperacion;
    }

    public void setEstadoOperacion(Integer estadoOperacion) {
        this.estadoOperacion = estadoOperacion;
    }
}
