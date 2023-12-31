package org.oefa.gob.pe.osigner.task.platform;

import javafx.concurrent.Task;
import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.application.PlatformService;
import org.oefa.gob.pe.osigner.commons.AppProcess;
import org.oefa.gob.pe.osigner.commons.AppType;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.core.component.CertificateComponent;
import org.oefa.gob.pe.osigner.core.component.StepComponent;
import org.oefa.gob.pe.osigner.domain.CertificateModel;
import org.oefa.gob.pe.osigner.domain.SignConfiguration;
import org.oefa.gob.pe.osigner.domain.fx.PlatformModel;
import org.oefa.gob.pe.osigner.util.TaskUtil;
import java.util.List;

public class SignTaskManager {

    public static DownloadFilesTask DOWNLOAD_FILE_TASK;
    public static UnzipFilesTask UNZIP_FILE_TASK;
    public static ConvertFilesTask CONVERT_FILE_TASK;
    public static GlosaTask GLOSA_FILE_TASK;
    public static SignPositionTask SIGNATURE_POSITION_TASK;
    public static CertificateTask CERTIFICATE_TASK;
    public static SignFilesTask SIGN_FILE_TASK;
    public static ZipFilesTask ZIP_FILE_TASK;
    public static UploadFilesTask UPLOAD_FILE_TASK;
    public static CertificateModel CERTIFICATE_MODEL;

    public static void initializeSignProccess(PlatformModel platformModel){
        StepComponent.STEP_LIST = platformModel.getSteps();
        CertificateComponent.CERT_COMBOBOX = platformModel.getCertificateComboBox();

        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN)){
            UNZIP_FILE_TASK = new UnzipFilesTask();
            CONVERT_FILE_TASK = new ConvertFilesTask();
            GLOSA_FILE_TASK = new GlosaTask();
            SIGNATURE_POSITION_TASK = new SignPositionTask();
            ZIP_FILE_TASK = new ZipFilesTask();
            UPLOAD_FILE_TASK = new UploadFilesTask();
        }

        SIGN_FILE_TASK = new SignFilesTask();
        DOWNLOAD_FILE_TASK = new DownloadFilesTask();
        UPLOAD_FILE_TASK = new UploadFilesTask();

        startSignProccess();

    }


    private static void startSignProccess(){
        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN)) {
            NotificationFX.initializeAndShowProgressNotification("Obteniendo archivos", "Descargando archivos...");
            TaskUtil.executeTasksOnSerial(
                    List.of(DOWNLOAD_FILE_TASK, UNZIP_FILE_TASK, CONVERT_FILE_TASK, SIGNATURE_POSITION_TASK, GLOSA_FILE_TASK)
            );

        }else{
            TaskUtil.executeTask(DOWNLOAD_FILE_TASK);
        }

    }


    public static void completeSignProccess(String certificateAlias){
        CERTIFICATE_TASK = new CertificateTask(certificateAlias);

        if(AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN)){
            NotificationFX.initializeAndShowProgressNotification("Completando proceso", "Firmando archivos...");
            TaskUtil.executeTasksOnSerial(
                    List.of(CERTIFICATE_TASK, SIGN_FILE_TASK, ZIP_FILE_TASK, UPLOAD_FILE_TASK)
            );

        }else{
            TaskUtil.executeTasksOnSerial(
                    List.of(CERTIFICATE_TASK, SIGN_FILE_TASK, UPLOAD_FILE_TASK)
            );
        }

    }


    public static void cancelSignProccess(){
        CancelTask cancelTask = new CancelTask();
        TaskUtil.executeTask(cancelTask);

    }


    public static void completeTask(AppProcess appProcess){
        switch (appProcess){
            case CONVERT_FILE -> {
                StepComponent.showStepLoading(0);
                CONVERT_FILE_TASK.succeeded();
                TaskUtil.executeTasksOnSerial(List.of(SIGNATURE_POSITION_TASK, GLOSA_FILE_TASK));
            }
            case SIGNATURE_POSITION -> {
                StepComponent.showStepLoading(0);
                SIGNATURE_POSITION_TASK.succeeded();
                TaskUtil.executeTask(GLOSA_FILE_TASK);
            }
            case GLOSA -> {
                StepComponent.showStepLoading(0);
                GLOSA_FILE_TASK.succeeded();
            }
            case SIGN_FILE -> {
                StepComponent.showStepLoading(2);
                SignConfiguration.getInstance().getSignProcessConfiguration().setTimeStamp(false);
                List<Task<Void>> lista = AppConfiguration.APP_TYPE.equals(AppType.MASSIVE_SIGN) ?
                        List.of(new SignFilesTask(), ZIP_FILE_TASK, UPLOAD_FILE_TASK) : List.of(new SignFilesTask(), UPLOAD_FILE_TASK);
                TaskUtil.executeTasksOnSerial(lista);
            }
        }
    }

}
