package io.github.winnpixie.logging.pipes;

import io.github.winnpixie.logging.LogItem;
import io.github.winnpixie.logging.OutputPipe;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaOutputPipe implements OutputPipe {
    private final Logger logger;

    public JavaOutputPipe(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(LogItem item) {
        switch (item.level()) {
            case DEBUG:
                logger.log(Level.CONFIG, item.throwable(),
                        () -> String.format(item.message(), item.parameters()));
                break;
            case INFO:
                logger.log(Level.INFO, item.throwable(),
                        () -> String.format(item.message(), item.parameters()));
                break;
            case WARNING:
                logger.log(Level.WARNING, item.throwable(),
                        () -> String.format(item.message(), item.parameters()));
                break;
            case SEVERE:
                logger.log(Level.SEVERE, item.throwable(),
                        () -> String.format(item.message(), item.parameters()));
                break;
        }
    }
}
