package org.oefa.gob.pe.osigner.infra.input.adapter;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.oefa.gob.pe.osigner.application.ProgressService;
import org.oefa.gob.pe.osigner.domain.fx.NotificationModel;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgressController implements Initializable {

    @FXML
    private Label textLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private MFXProgressBar progressBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProgressService.NOTIFICATION_MODEL = new NotificationModel(textLabel, titleLabel, progressBar);
    }

    @FXML
    void closeProgress(MouseEvent event) {
        ProgressService.closeProgressNotification();

    }

}
