package io.github.winnpixie.logging.outputs;

import io.github.winnpixie.logging.OutputDevice;

public class SystemOutputDevice implements OutputDevice {
    @Override
    public void info(String message) {
        System.out.printf("[INFO] %s%n", message);
    }

    @Override
    public void warn(String warning) {
        System.out.printf("[WARN] %s%n", warning);
    }

    @Override
    public void error(String error) {
        System.err.printf("[ERROR] %s%n", error);
    }
}
