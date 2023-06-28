package org.oefa.gob.pe.osigner.domain;

import java.security.PrivateKey;

public class CertificateModel {
    private String alias;
    private String nombre;
    private String fechaEmision;
    private String fechaCaducidad;
    private String numeroDocumento;
    private PrivateKey key;
    private java.security.cert.Certificate[] chain;

    public CertificateModel(String alias, String nombre, String fechaEmision, String fechaCaducidad, String numeroDocumento) {
        this.alias = alias;
        this.nombre = nombre;
        this.fechaEmision = fechaEmision;
        this.fechaCaducidad = fechaCaducidad;
        this.numeroDocumento = numeroDocumento;
    }

    public PrivateKey getKey() {
        return key;
    }

    public void setKey(PrivateKey key) {
        this.key = key;
    }

    public java.security.cert.Certificate[] getChain() {
        return chain;
    }

    public void setChain(java.security.cert.Certificate[] chain) {
        this.chain = chain;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
}
