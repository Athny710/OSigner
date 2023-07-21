package org.oefa.gob.pe.osigner.domain.ws;

public class SignatureImage {

    private byte[] image;
    private Integer estado;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
