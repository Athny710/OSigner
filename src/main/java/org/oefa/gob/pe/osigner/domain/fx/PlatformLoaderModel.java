package org.oefa.gob.pe.osigner.domain.fx;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class PlatformLoaderModel {

    private StackPane mainContainer;
    private GridPane platformLoaderContainer;

    public PlatformLoaderModel(StackPane mainContainer, GridPane platformLoaderContainer) {
        this.mainContainer = mainContainer;
        this.platformLoaderContainer = platformLoaderContainer;
    }

    public StackPane getMainContainer() {
        return mainContainer;
    }

    public void setMainContainer(StackPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    public GridPane getPlatformLoaderContainer() {
        return platformLoaderContainer;
    }

    public void setPlatformLoaderContainer(GridPane platformLoaderContainer) {
        this.platformLoaderContainer = platformLoaderContainer;
    }
}
