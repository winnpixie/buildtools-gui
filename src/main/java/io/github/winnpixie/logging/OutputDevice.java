package io.github.winnpixie.logging;

public interface OutputDevice {
    default void info(String message) {
        print(String.format("[INFO] %s", message));
    }

    default void warn(String warning) {
        print(String.format("[WARN] %s", warning));
    }

    default void error(String error) {
        print(String.format("[ERROR] %s", error));
    }

    void print(String message);
}
