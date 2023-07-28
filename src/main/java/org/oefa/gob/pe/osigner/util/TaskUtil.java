package org.oefa.gob.pe.osigner.util;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskUtil {

    public static void executeTask(Task task){
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    public static void executeTwoTasksOnSerial(List<Task<Void>> taskList) {
        ExecutorService executorService  = Executors.newSingleThreadExecutor();

        taskList.get(0).addEventHandler(
                WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                workerStateEvent -> {
                    executorService.submit(taskList.get(1));
                }
        );

        executorService.submit(taskList.get(0));


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
