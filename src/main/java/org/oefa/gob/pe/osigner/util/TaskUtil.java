package org.oefa.gob.pe.osigner.util;

import javafx.concurrent.Task;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskUtil {

    public static void executeTask(Task task){
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    public static void executeTasksOnSerial(List<Task<Void>> taskList){
        ExecutorService executorService  = Executors.newSingleThreadExecutor();
        for (Task<Void> task : taskList) {
            executorService.submit(task);
        }

    }

    public static void waitToContinue(long duration){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(duration);
                return null;
            }
        };

       executeTask(task);
    }
}
