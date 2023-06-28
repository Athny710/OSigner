package org.oefa.gob.pe.osigner;

import javafx.application.Application;
import javafx.stage.Stage;
import org.oefa.gob.pe.osigner.core.RouterFX;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        ApplicationModel.NOTIFICATION_STAGE = new Stage();
        ApplicationModel.CURRENT_STAGE = stage;
        RouterFX.intializeApp(getParameters());

    }

    public static void main(String[] args) {
        LogUtil.setInfo("Iniciado aplicación", MainApplication.class.getName());
        launch(args);

    }
}