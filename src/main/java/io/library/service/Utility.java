package io.library.service;

import java.util.Scanner;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class Utility {
    private static final Logger logger = LoggingService.getLogger(Utility.class);
    private static final Scanner scanner = new Scanner(System.in);

    public static String getFormattedString(String value) {
        return new StringJoiner("", "'", "'").add(value).toString();
    }

    public static synchronized Scanner getScanner() {
        return scanner;
    }
    public static void closeScanner() {
        logger.info("Scanner closed");
        scanner.close();
    }

}
