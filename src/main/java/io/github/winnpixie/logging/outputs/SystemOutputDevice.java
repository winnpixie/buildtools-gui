package io.github.winnpixie.logging.outputs;

import io.github.winnpixie.logging.OutputDevice;

public class SystemOutputDevice implements OutputDevice {
    @Override
    public void error(String error) {
        System.err.printf("[ERROR] %s%n", error);
    }

    @Override
    public void print(String message) {
        System.out.printf("%s%n", message);
    }
}
