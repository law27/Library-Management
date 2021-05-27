package io.library.service;

import java.util.Scanner;
import java.util.StringJoiner;

public class Utility {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getFormattedString(String value) {
        return new StringJoiner("", "'", "'").add(value).toString();
    }

    public static synchronized Scanner getScanner() {
        return scanner;
    }
    public static void closeScanner() {
        scanner.close();
    }

}
