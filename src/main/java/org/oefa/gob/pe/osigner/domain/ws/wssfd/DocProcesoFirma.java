package org.oefa.gob.pe.osigner.domain.ws.wssfd;

public class DocProcesoFirma {

    private Integer id;
    private String documento;
    private byte[] bytes;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
