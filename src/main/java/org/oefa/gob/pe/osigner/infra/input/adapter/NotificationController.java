package org.oefa.gob.pe.osigner.infra.input.adapter;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.oefa.gob.pe.osigner.application.NotificationService;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;
import org.oefa.gob.pe.osigner.domain.fx.NotificationModel;
import java.net.URL;
import java.util.ResourceBundle;

public class NotificationController implements Initializable {

    @FXML
    private Label textLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private MFXButton notificationConfirmButton;
    @FXML
    private MFXButton notificationCancelButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NotificationService.NOTIFICATION_MODEL = new NotificationModel(this.textLabel, this.titleLabel, this.notificationConfirmButton, this.notificationCancelButton);
    }

}
