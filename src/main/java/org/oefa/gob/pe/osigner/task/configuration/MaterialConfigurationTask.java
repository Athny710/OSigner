package org.oefa.gob.pe.osigner.task.configuration;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.domain.fx.ApplicationModel;
import org.oefa.gob.pe.osigner.util.LogUtil;

public class MaterialConfigurationTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        Thread.sleep(400);
        MFXThemeManager.addOn(ApplicationModel.CURRENT_STAGE.getScene(), Themes.DEFAULT, Themes.LEGACY);

        return null;

    }

    @Override
    protected void succeeded() {
        LogUtil.setInfo("[CONFIGURACION] Se configuró la aplicación con MaterialFX", this.getClass().getName());
        super.succeeded();
        ConfigurationTaskManager.setConfigurationFinished();

    }


    @Override
    protected void failed() {
        super.failed();
        String errorMessage = LogUtil.setError(
                "Error configurando MaterialFX en la aplicación",
                this.getClass().getName(),
                (Exception) super.getException()
        );

        NotificationFX.showMaterialFxErrorNotification(errorMessage);

    }
}
