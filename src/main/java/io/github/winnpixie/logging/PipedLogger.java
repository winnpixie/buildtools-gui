package io.github.winnpixie.logging;

import java.util.ArrayList;
import java.util.List;

public class PipedLogger {
    private final List<OutputPipe> pipes = new ArrayList<>();

    private LogLevel logLevel = LogLevel.INFO;

    public PipedLogger(OutputPipe... pipes) {
        for (OutputPipe device : pipes) addPipe(device);
    }

    public List<OutputPipe> getPipes() {
        return pipes;
    }

    public boolean addPipe(OutputPipe pipe) {
        return pipes.add(pipe);
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public void debug(String message, Object... parameters) {
        log(LogLevel.DEBUG, message, parameters);
    }

    public void info(String message, Object... parameters) {
        log(LogLevel.INFO, message, parameters);
    }

    public void warning(String message, Object... parameters) {
        log(LogLevel.WARNING, message, parameters);
    }

    public void severe(String message, Object... parameters) {
        log(LogLevel.SEVERE, message, parameters);
    }

    public void log(LogLevel type, String message, Object... parameters) {
        log(type, null, message, parameters);
    }

    public void log(LogLevel type, Throwable thrown, String message, Object... parameters) {
        print(new LogItem(type, message, parameters, thrown));
    }

    private boolean hasPriority(LogItem item) {
        return item.level().priority() >= logLevel.priority();
    }

    public void print(LogItem item) {
        if (!hasPriority(item)) return;

        for (OutputPipe pipe : pipes) pipe.print(item);
    }
}
