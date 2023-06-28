package org.oefa.gob.pe.osigner.util;

import javafx.concurrent.Task;

public class TaskUtil {

    public static void executeTask(Task task){
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }
}
