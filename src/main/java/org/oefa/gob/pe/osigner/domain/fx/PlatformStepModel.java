package org.oefa.gob.pe.osigner.domain.fx;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PlatformStepModel {


    private ImageView imageView;
    private Label label;


    public PlatformStepModel(ImageView imageView, Label label) {
        this.imageView = imageView;
        this.label = label;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
