package org.oefa.gob.pe.osigner.core;

import javafx.animation.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimationFX {

    private static double X_CENTER;
    private static double Y_CENTER;


    public static void displayStage(Stage stage){
        calculateCenter();

        Animation transition = new Transition() {
            {
                setCycleDuration(Duration.millis(400));
                setInterpolator(Interpolator.EASE_BOTH);
            }
            @Override
            protected void interpolate(double frac) {
                stage.setHeight(380*frac);
                stage.setWidth(320*frac);
                stage.setX(X_CENTER - 160);
                stage.setY(Y_CENTER - 190);
            }
        };
        transition.play();

    }

    public static void displayPlatformView(Parent currentParent){
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(currentParent.translateYProperty(), -currentParent.getScene().getHeight(), Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(1.0), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    public static void displayNotification(Stage stage){
        Animation transition = new Transition() {
            {
                setCycleDuration(Duration.millis(300));
                setInterpolator(Interpolator.EASE_BOTH);
            }
            @Override
            protected void interpolate(double frac) {
                stage.setHeight(173*frac);
                stage.setWidth(320*frac);
                stage.setX(2*X_CENTER - stage.getWidth() - 10);
                stage.setY(2*Y_CENTER - stage.getHeight() - 10);
            }
        };
        transition.play();

    }

    private static void calculateCenter(){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        X_CENTER = screenBounds.getWidth()/2;
        Y_CENTER = screenBounds.getHeight()/2;

    }
}
