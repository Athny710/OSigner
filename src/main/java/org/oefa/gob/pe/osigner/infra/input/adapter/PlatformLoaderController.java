package org.oefa.gob.pe.osigner.infra.input.adapter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.oefa.gob.pe.osigner.application.PlatformLoaderService;
import org.oefa.gob.pe.osigner.core.ControllerFX;
import org.oefa.gob.pe.osigner.core.ResourceFX;
import org.oefa.gob.pe.osigner.domain.fx.PlatformLoaderModel;
import java.net.URL;
import java.util.ResourceBundle;


public class PlatformLoaderController extends ControllerFX implements Initializable {

    @FXML
    private StackPane mainContainer;

    @FXML
    private GridPane platformLoaderContainer;

    @FXML
    private ImageView imgFullLogoContainer;

    @FXML
    private ImageView imgLogoCFDContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PlatformLoaderService.platformLoaderModel = new PlatformLoaderModel(this.mainContainer, this.platformLoaderContainer);
        Image imageOEFA = new Image(ResourceFX.load("img/Oefa_small_logo.png"), 103 , 36, true, true);
        Image imageCFD = new Image(ResourceFX.load("img/CFD_full_logo.png"), 356, 82, true, true);
        imgFullLogoContainer.setImage(imageOEFA);
        imgLogoCFDContainer.setImage(imageCFD);

    }


}
