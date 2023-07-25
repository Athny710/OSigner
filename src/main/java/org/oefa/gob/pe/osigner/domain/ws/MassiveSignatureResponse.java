package org.oefa.gob.pe.osigner.domain.ws;

import org.oefa.gob.pe.osigner.domain.ws.wssfd.ArchivoFirmaMasiva;
import org.oefa.gob.pe.osigner.domain.ws.wssfd.ConfiguracionFirmaMasiva;

import java.util.ArrayList;

public class MassiveSignatureResponse {
    private ConfiguracionFirmaMasiva configuracionFirma;
    private ArrayList<ArchivoFirmaMasiva> listaArchivos;
    private byte[] imagenFirma;

    public ConfiguracionFirmaMasiva getConfiguracionFirma() {
        return configuracionFirma;
    }

    public void setConfiguracionFirma(ConfiguracionFirmaMasiva configuracionFirma) {
        this.configuracionFirma = configuracionFirma;
    }

    public ArrayList<ArchivoFirmaMasiva> getListaArchivos() {
        return listaArchivos;
    }

    public void setListaArchivos(ArrayList<ArchivoFirmaMasiva> listaArchivos) {
        this.listaArchivos = listaArchivos;
    }

    public byte[] getImagenFirma() {
        return imagenFirma;
    }

    public void setImagenFirma(byte[] imagenFirma) {
        this.imagenFirma = imagenFirma;
    }
}
