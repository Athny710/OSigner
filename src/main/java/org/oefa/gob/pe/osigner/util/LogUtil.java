package org.oefa.gob.pe.osigner.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {

    public static void setInfo(String message, String className){
        Logger logger = LogManager.getLogger(className);
        logger.info(message);
    }

    public static String setError(String message, String className, Exception e){
        String finalMessage = message + "\n" + ExceptionUtils.getStackTrace(e);

        Logger logger = LogManager.getLogger(className);
        logger.error(finalMessage);

        return finalMessage;

    }

}
