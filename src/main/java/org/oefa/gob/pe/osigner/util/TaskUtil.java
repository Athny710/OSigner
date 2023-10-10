package org.oefa.gob.pe.osigner.util;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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

        for(int i=0; i< taskList.size() - 1; i++){
            int nextValue = i + 1;
            taskList.get(i).setOnSucceeded(
                    workerStateEvent -> {
                        executorService.submit(taskList.get(nextValue));
                    }
            );
        }

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
