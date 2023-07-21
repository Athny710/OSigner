package org.oefa.gob.pe.osigner.infra.input.adapter;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.domain.fx.PlatformModel;
import org.oefa.gob.pe.osigner.domain.fx.PlatformStepModel;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PlatformController implements Initializable {

    @FXML
    private MFXComboBox<String> certificateComboBox;

    @FXML
    private MFXButton cancelButton;

    @FXML
    private MFXButton confirmButton;

    @FXML
    private Button updateButton;

    @FXML
    private ImageView imgView_1;

    @FXML
    private ImageView imgView_2;

    @FXML
    private ImageView imgView_3;

    @FXML
    private ImageView imgView_4;

    @FXML
    private Label stepLabel_1;

    @FXML
    private Label stepLabel_2;

    @FXML
    private Label stepLabel_3;

    @FXML
    private Label stepLabel_4;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PlatformStepModel stepModel_1 = new PlatformStepModel(this.imgView_1, this.stepLabel_1);
        PlatformStepModel stepModel_2 = new PlatformStepModel(this.imgView_2, this.stepLabel_2);
        PlatformStepModel stepModel_3 = new PlatformStepModel(this.imgView_3, this.stepLabel_3);
        PlatformStepModel stepModel_4 = new PlatformStepModel(this.imgView_4, this.stepLabel_4);
        List<PlatformStepModel> stepList = List.of(stepModel_1, stepModel_2, stepModel_3, stepModel_4);

        PlatformService.PLATFORM_MODEL = new PlatformModel(stepList, this.certificateComboBox, this.confirmButton, this.cancelButton, this.updateButton);
    }

    @FXML
    void onCancel(ActionEvent event) {
        PlatformService.showErrorAndClose();

    }

    @FXML
    void onConfirm(ActionEvent event) {
        PlatformService.completeSignProccess(certificateComboBox.getValue());

    }

    @FXML
    void onUpdate(ActionEvent event) {
        PlatformService.reloadCertificates();

    }

}
