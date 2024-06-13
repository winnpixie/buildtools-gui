package io.github.winnpixie.logging.outputs;

import io.github.winnpixie.logging.OutputDevice;

import java.util.logging.Logger;

public class JavaOutputDevice implements OutputDevice {
    private final Logger logger;

    public JavaOutputDevice(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String warning) {
        logger.warning(warning);
    }

    @Override
    public void error(String error) {
        logger.severe(error);
    }

    @Override
    public void print(String message) {
        info(message);
    }
}
