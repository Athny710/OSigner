package org.oefa.gob.pe.osigner.core;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.oefa.gob.pe.osigner.MainApplication;
import org.oefa.gob.pe.osigner.application.PlatformLoaderService;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;
import org.oefa.gob.pe.osigner.util.LogUtil;
import org.oefa.gob.pe.osigner.util.TaskUtil;


public class AppFX {


    /**
     * Función que se encarga de mostrar la vista del Loader para FIRMA MASIVA y FIRMA SIMPLE.
     * @throws Exception Exeption al cargar la vista y su controlador.
     */
    public static void showPlatformLoader() throws Exception {
        Scene scene = loadScene("view/PlatformLoader.fxml");
        Stage stage = ApplicationModel.CURRENT_STAGE;

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setOnShowing(e -> {
            // Una vez mostrada la vista se empieza con la ejecución de la aplicación.
            PlatformLoaderService.initializeConfiguration();
        });
        stage.show();

        AnimationFX.displayStage(stage);

    }

    /**
     * Función
     * @param resource Archivo FXML de la vista que se va a cargar.
     * @param notificaionStage Stage al que se le cargará la vista.
     */
    public static void showNotification(String resource, Stage notificaionStage) {
        try {
            Scene parentScene = ApplicationModel.CURRENT_SCENE;
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resource));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);

            if(notificaionStage.getStyle() != StageStyle.TRANSPARENT) {
                notificaionStage.initStyle(StageStyle.TRANSPARENT);
                notificaionStage.initModality(Modality.WINDOW_MODAL);
                notificaionStage.initOwner(parentScene.getWindow());
            }

            notificaionStage.setScene(scene);
            notificaionStage.show();

            MFXThemeManager.addOn(notificaionStage.getScene(), Themes.DEFAULT, Themes.LEGACY);
            AnimationFX.displayNotification(notificaionStage);

        } catch (Exception e){
            LogUtil.setError(
                    "Error cargando la notificación",
                    AppFX.class.getName(),
                    e
            );
        }

    }

    /**
     * Función que se encarga de mostrar las notificaciones de error.
     */
    public static void showNotificationError() {
        showNotification("view/Notification.fxml", ApplicationModel.NOTIFICATION_STAGE);

    }

    /**
     * Función que se encarga de mostrar las notificaciones de progreso de procesos.
     */
    public static void showProgressNotification(){
        showNotification("view/Progress.fxml", ApplicationModel.PROGRESS_STAGE);
    }


    /**
     * Función que obtiene el componente padre de la vista indicada.
     * @param fxmlResource Archivo FXML de la vista que se va a mostrar.
     * @return Componente padre.
     * @throws Exception Excepción al momento de cargar la vista indicada.
     */
    public static Parent getParent(String fxmlResource) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlResource));
        return fxmlLoader.load();

    }


    /**
     * Función que se encarga de cargar la Scene de cada archivo FXML y lo guarda de forma global.
     * @param fxmlResource Archivo FXML de la vista que se desea mostrar.
     * @return Scene de la vista.
     * @throws Exception Excepción al momento de cargar la vista indicada.
     */
    public static Scene loadScene(String fxmlResource) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlResource));
        Parent parent = fxmlLoader.load();

        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);

        ApplicationModel.CURRENT_SCENE = scene;
        return scene;

    }


    /**
     * Función que cierra y termina la aplicación.
     */
    public static void closeApplication(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Tiempo que se tomará para mostrar vista/mensaje final.
                Thread.sleep(Constant.CLOSE_APP_DELAY_TIME);
                AnimationFX.closeApplication(ApplicationModel.CURRENT_STAGE);
                // Tiempo que se tomará para la animación cuando se cierra la aplicación.
                Thread.sleep(Constant.EXIT_APP_DELAY_TIME);
                System.exit(1);

                return null;
            }
        };
        TaskUtil.executeTask(task);

    }
}
