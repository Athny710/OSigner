package org.oefa.gob.pe.osigner.core.component;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;

public class CertificateComponent {

    public static MFXComboBox<String> CERT_COMBOBOX;

    public static void loadCertificates(ArrayList<String> certList){
        CERT_COMBOBOX.getItems().removeAll(CERT_COMBOBOX.getItems());
        List<String> list = (certList.size() == 0) ? List.of("No hay certificados disponibles") : certList;
        CERT_COMBOBOX.setItems(FXCollections.observableList(certList));

    }
}
