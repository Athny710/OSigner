package org.oefa.gob.pe.osigner.infra.input.adapter;

import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
        ProgressService.PROGRESS_MODEL = new NotificationModel(textLabel, titleLabel, progressBar);
        Bindings.bindBidirectional(this.textLabel.textProperty(), ProgressService.PROGRESS_MODEL.textLabelProperty());
        Bindings.bindBidirectional(this.progressBar.progressProperty(), ProgressService.PROGRESS_MODEL.progressBarProperty());
    }

    @FXML
    void closeProgress(MouseEvent event) {
        ProgressService.closeProgressNotification();

    }

}
