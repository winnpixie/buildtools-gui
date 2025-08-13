package io.github.winnpixie.logging;

public enum LogLevel {
    DEBUG(-1),
    INFO(1),
    WARNING(2),
    SEVERE(3);

    private final int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }
}
