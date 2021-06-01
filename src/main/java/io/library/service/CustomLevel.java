package io.library.service;

import java.util.logging.Level;

public class CustomLevel extends Level {
    public static final CustomLevel ERROR = new CustomLevel("ERROR", 200);

    protected CustomLevel(String name, int value) {
        super(name, value);
    }
}
