package io.library.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingService {

    private static FileHandler fileHandler;
    private static String getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return formatter.format(date);
    }

    private static FileHandler getFileHandler() throws IOException {
        String path = "src/main/log/";
        String file = path + getFormattedDate() + ".log";
        if(fileHandler == null) {
            fileHandler = new FileHandler(file, true);
        }
        return fileHandler;
    }

    public static synchronized Logger getLogger(Class<?> className) {
        Logger logger = Logger.getLogger(className.getName());
        FileHandler fileHandler;
        try {
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler = getFileHandler();
            fileHandler.setFormatter(formatter);
            logger.setUseParentHandlers(false);
            logger.addHandler(fileHandler);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        return logger;
    }

    public static void closeHandler() {
        if(fileHandler != null) {
            fileHandler.close();
        }
    }

}
