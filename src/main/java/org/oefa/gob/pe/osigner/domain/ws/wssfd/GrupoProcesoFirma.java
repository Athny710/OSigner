package org.oefa.gob.pe.osigner.domain.ws.wssfd;

import java.util.List;

public class GrupoProcesoFirma {

    private Integer id;
    private String ipCliente;
    private Integer cantidad;
    private String nameConf;
    private byte[] bytesConf;
    private String fechaRegistro;
    private String estado;
    private Proceso proceso;
    private List<DocProcesoFirma> listDocs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpCliente() {
        return ipCliente;
    }

    public void setIpCliente(String ipCliente) {
        this.ipCliente = ipCliente;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNameConf() {
        return nameConf;
    }

    public void setNameConf(String nameConf) {
        this.nameConf = nameConf;
    }

    public byte[] getBytesConf() {
        return bytesConf;
    }

    public void setBytesConf(byte[] bytesConf) {
        this.bytesConf = bytesConf;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public List<DocProcesoFirma> getListDocs() {
        return listDocs;
    }

    public void setListDocs(List<DocProcesoFirma> listDocs) {
        this.listDocs = listDocs;
    }
}
