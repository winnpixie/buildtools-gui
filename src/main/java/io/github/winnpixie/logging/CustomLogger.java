package io.github.winnpixie.logging;

import java.util.ArrayList;
import java.util.List;

public class CustomLogger {
    private final List<OutputDevice> outputDevices = new ArrayList<>();

    public CustomLogger(OutputDevice... outputDevices) {
        for (OutputDevice device : outputDevices) {
            addOutput(device);
        }
    }

    public boolean addOutput(OutputDevice outputDevice) {
        return outputDevices.add(outputDevice);
    }

    public void info(String message) {
        outputDevices.forEach(device -> device.info(message));
    }

    public void warn(String warning) {
        outputDevices.forEach(device -> device.warn(warning));
    }

    public void error(String error) {
        outputDevices.forEach(device -> device.error(error));
    }

    public void error(Throwable throwable) {
        outputDevices.forEach(device -> device.error(String.format("%s: %s", throwable.getClass().getSimpleName(),
                throwable.getMessage())));
    }

    public void print(String message) {
        outputDevices.forEach(device -> device.print(message));
    }
}
