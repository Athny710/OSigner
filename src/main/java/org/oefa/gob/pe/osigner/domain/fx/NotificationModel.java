package org.oefa.gob.pe.osigner.domain.fx;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.control.Label;

public class NotificationModel {


    private Label textLabel;
    private Label titleLabel;
    private MFXButton confirmButton;
    private MFXButton cancelButton;



    public NotificationModel(Label textLabel, Label titleLabel, MFXButton confirmButton, MFXButton cancelButton) {
        this.textLabel = textLabel;
        this.titleLabel = titleLabel;
        this.confirmButton = confirmButton;
        this.cancelButton = cancelButton;
    }

    public Label getTextLabel() {
        return textLabel;
    }

    public void setTextLabel(Label textLabel) {
        this.textLabel = textLabel;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(Label titleLabel) {
        this.titleLabel = titleLabel;
    }

    public MFXButton getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(MFXButton cancelButton) {
        this.cancelButton = cancelButton;
    }

    public MFXButton getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(MFXButton confirmButton) {
        this.confirmButton = confirmButton;
    }
}
