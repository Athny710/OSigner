package org.oefa.gob.pe.osigner.core.component;

import javafx.scene.image.Image;
import org.oefa.gob.pe.osigner.core.ResourceFX;
import org.oefa.gob.pe.osigner.domain.fx.PlatformStepModel;
import java.util.List;

public class StepComponent {


    public static List<PlatformStepModel> STEP_LIST;
    private static final List<String> SUCCESS_STEPS_TEXT_LIST = List.of(
            "Se descargaron los archivos",
            "Confirmación exitosa",
            "Se firmaron los archivos",
            "Se subieron los archivos"
    );
    enum COMPLETED_TYPE{
        SUCCESS, ERROR
    }


    public static void showStepCompleted(int stepNumber){
        updateImg(COMPLETED_TYPE.SUCCESS, stepNumber);
        updateText(stepNumber);
        loadNextStep(stepNumber + 1);

    }



    public static void showStepError(int stepNumber){
        updateImg(COMPLETED_TYPE.ERROR, stepNumber);
    }


    private static void loadNextStep(int stepNumber){
        if(stepNumber >= STEP_LIST.size())
            return;

        STEP_LIST.get(stepNumber).getLabel().getStyleClass().removeAll("inactive-text");
        STEP_LIST.get(stepNumber).getLabel().getStyleClass().add("active-text");

    }


    private static void updateText(int stepNumber){
        STEP_LIST.get(stepNumber).getLabel().setText(SUCCESS_STEPS_TEXT_LIST.get(stepNumber));

    }


    private static void updateImg(COMPLETED_TYPE type, int stepNumber){
        String resource = type == COMPLETED_TYPE.SUCCESS  ? "icon/Success.png" : "icon/Error.png";
        Image image = new Image(ResourceFX.load(resource));
        STEP_LIST.get(stepNumber).getImageView().setImage(image);

    }

}
