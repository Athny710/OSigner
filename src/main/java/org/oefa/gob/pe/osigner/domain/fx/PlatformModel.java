package org.oefa.gob.pe.osigner.domain.fx;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.scene.control.Button;

import java.util.List;

public class PlatformModel {


    private List<PlatformStepModel> steps;
    private MFXComboBox<String> certificateComboBox;
    private Button confirmButton;
    private Button cancelButton;
    private Button updateButton;


    public PlatformModel(List<PlatformStepModel> steps,MFXComboBox<String> certificateComboBox, Button confirmButton, Button cancelButton, Button updateButton) {
        this.certificateComboBox = certificateComboBox;
        this.confirmButton = confirmButton;
        this.cancelButton = cancelButton;
        this.updateButton = updateButton;
        this.steps = steps;
    }


    public List<PlatformStepModel> getSteps() {
        return steps;
    }

    public void setSteps(List<PlatformStepModel> steps) {
        this.steps = steps;
    }

    public MFXComboBox<String> getCertificateComboBox() {
        return certificateComboBox;
    }

    public void setCertificateComboBox(MFXComboBox<String> certificateComboBox) {
        this.certificateComboBox = certificateComboBox;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(Button confirmButton) {
        this.confirmButton = confirmButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public void setUpdateButton(Button updateButton) {
        this.updateButton = updateButton;
    }
}
