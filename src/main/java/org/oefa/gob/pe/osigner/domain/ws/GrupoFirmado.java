package org.oefa.gob.pe.osigner.domain.ws;

import java.util.ArrayList;
import java.util.List;

public class GrupoFirmado {

    private Integer id;
    private ArrayList<String> documentosNames;
    private List<byte[]> documentosBytes;

    public GrupoFirmado(Integer id, ArrayList<String> documentosNames, List<byte[]> documentosBytes) {
        this.id = id;
        this.documentosNames = documentosNames;
        this.documentosBytes = documentosBytes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<String> getDocumentosNames() {
        return documentosNames;
    }

    public void setDocumentosNames(ArrayList<String> documentosNames) {
        this.documentosNames = documentosNames;
    }

    public List<byte[]> getDocumentosBytes() {
        return documentosBytes;
    }

    public void setDocumentosBytes(List<byte[]> documentosBytes) {
        this.documentosBytes = documentosBytes;
    }
}
