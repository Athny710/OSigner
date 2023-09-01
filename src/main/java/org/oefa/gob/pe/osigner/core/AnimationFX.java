package org.oefa.gob.pe.osigner.core;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.oefa.gob.pe.osigner.application.PlatformLoaderService;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;

public class AnimationFX {

    public static void displayStage(Stage stage){
        stage.getScene().getRoot().setScaleX(0);
        stage.getScene().getRoot().setScaleY(0);

        KeyValue kv_width = new KeyValue(stage.getScene().getRoot().scaleXProperty(), 1,Interpolator.EASE_BOTH);
        KeyValue kv_height = new KeyValue(stage.getScene().getRoot().scaleYProperty(), 1, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(200), kv_width, kv_height);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.play();

    }

    public static void closeApplication(Stage stage){
        KeyValue kv_width = new KeyValue(stage.getScene().getRoot().scaleXProperty(), 0,Interpolator.EASE_BOTH);
        KeyValue kv_height = new KeyValue(stage.getScene().getRoot().scaleYProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(Constant.EXIT_APP_DELAY_TIME), kv_width, kv_height);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.play();

    }

    public static void displayPlatformView(Parent parentToAnimate){
        KeyValue kv = new KeyValue(parentToAnimate.translateYProperty(), -parentToAnimate.getScene().getHeight(), Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.7), kv);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.play();

        new Thread(() -> {
            try {
                Thread.sleep(700);
                Platform.runLater(() -> {
                    PlatformLoaderService.platformLoaderModel.getMainContainer().getChildren().remove(1);
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    public static void displayNotification(Stage stage){
        stage.getScene().getRoot().setTranslateY(stage.getHeight());
        stage.setX(Screen.getPrimary().getVisualBounds().getWidth() - stage.getWidth() - 15);
        stage.setY(Screen.getPrimary().getVisualBounds().getHeight() - stage.getHeight() - 5);

        KeyValue kv_height = new KeyValue(stage.getScene().getRoot().translateYProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(400), kv_height);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.play();

    }

    public static void displayEndView(Parent parentToAnimate){
        parentToAnimate.setTranslateY(340);

        KeyValue kv = new KeyValue(parentToAnimate.translateYProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.7), kv);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.play();

    }

}
