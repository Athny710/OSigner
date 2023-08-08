package org.oefa.gob.pe.osigner.domain.fx;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;

public class NotificationModel {


    private Label textLabel;
    private Label titleLabel;
    private MFXButton confirmButton;
    private MFXButton cancelButton;
    private MFXProgressBar progressBar;

    private SimpleStringProperty textLabelProperty;
    private SimpleDoubleProperty progressBarProperty;

    public NotificationModel(Label textLabel, Label titleLabel, MFXProgressBar progressBar) {
        this.textLabel = textLabel;
        this.titleLabel = titleLabel;
        this.progressBar = progressBar;
        this.textLabelProperty = new SimpleStringProperty("");
        this.progressBarProperty = new SimpleDoubleProperty(0.0);
    }

    public NotificationModel(Label textLabel, Label titleLabel, MFXButton confirmButton, MFXButton cancelButton) {
        this.textLabel = textLabel;
        this.titleLabel = titleLabel;
        this.confirmButton = confirmButton;
        this.cancelButton = cancelButton;
    }

    public String gettextlabelpropertyValue() {
        return textLabelProperty.get();
    }

    public SimpleStringProperty textLabelProperty() {
        return textLabelProperty;
    }

    public void setTextLabelProperty(String taxtLabelProperty) {
        this.textLabelProperty.set(taxtLabelProperty);
    }

    public double getProgressBarPropertyValue() {
        return progressBarProperty.get();
    }

    public SimpleDoubleProperty progressBarProperty() {
        return progressBarProperty;
    }

    public void setProgressBarProperty(double progressBarProperty) {
        this.progressBarProperty.set(progressBarProperty);
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

    public MFXProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(MFXProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    public void setConfirmButton(MFXButton confirmButton) {
        this.confirmButton = confirmButton;
    }
}
