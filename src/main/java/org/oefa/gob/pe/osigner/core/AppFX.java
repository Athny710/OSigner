package org.oefa.gob.pe.osigner.core;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.oefa.gob.pe.osigner.MainApplication;
import org.oefa.gob.pe.osigner.application.PlatformLoaderService;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class AppFX {


    public static void showPlatformLoader() throws Exception {
        Scene scene = loadScene("view/PlatformLoader.fxml");
        Stage stage = ApplicationModel.CURRENT_STAGE;

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setOnShowing(e -> {
            PlatformLoaderService.initializeConfiguration();
        });
        stage.show();

        AnimationFX.displayStage(stage);

    }

    public static void showNotificationError() {
        try {
            Scene parentScene = ApplicationModel.CURRENT_SCENE;
            Scene scene = loadScene("view/Notification.fxml");
            Stage stage = ApplicationModel.NOTIFICATION_STAGE;

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentScene.getWindow());
            stage.show();
            MFXThemeManager.addOn(ApplicationModel.NOTIFICATION_STAGE.getScene(), Themes.DEFAULT, Themes.LEGACY);
            AnimationFX.displayNotification(stage);

        } catch (Exception e){
            LogUtil.setError("Error cargando la notificación", AppFX.class.getName(), e);
        }

    }

    public static Parent getParent(String fxmlResource) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlResource));
        return fxmlLoader.load();

    }

    public static Scene loadScene(String fxmlResource) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlResource));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);

        ApplicationModel.CURRENT_SCENE = scene;
        return scene;

    }
}
