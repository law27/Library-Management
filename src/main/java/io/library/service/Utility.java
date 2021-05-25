package io.library.service;

import java.util.StringJoiner;

public class Utility {
    public static String getFormattedString(String value) {
        return new StringJoiner("", "'", "'").add(value).toString();
    }
}
