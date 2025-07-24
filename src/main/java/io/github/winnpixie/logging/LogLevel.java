package io.github.winnpixie.logging;

public enum LogLevel {
    DEBUG(-1, "DEBUG"),
    INFO(1, "INFO"),
    WARNING(2, "WARNING"),
    SEVERE(3, "SEVERE");

    private final int priority;
    private final String prefix;

    LogLevel(int priority, String prefix) {
        this.priority = priority;
        this.prefix = prefix;
    }

    public int priority() {
        return priority;
    }

    public String prefix() {
        return prefix;
    }
}
