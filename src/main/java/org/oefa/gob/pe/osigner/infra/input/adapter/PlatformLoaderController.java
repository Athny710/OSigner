package org.oefa.gob.pe.osigner.infra.input.adapter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.oefa.gob.pe.osigner.application.PlatformLoaderService;
import org.oefa.gob.pe.osigner.core.ControllerFX;
import org.oefa.gob.pe.osigner.domain.fx.PlatformLoaderModel;
import java.net.URL;
import java.util.ResourceBundle;


public class PlatformLoaderController extends ControllerFX implements Initializable {

    @FXML
    private StackPane mainContainer;

    @FXML
    private GridPane platformLoaderContainer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PlatformLoaderService.platformLoaderModel = new PlatformLoaderModel(this.mainContainer, this.platformLoaderContainer);

    }


}
