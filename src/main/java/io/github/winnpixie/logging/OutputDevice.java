package io.github.winnpixie.logging;

public interface OutputDevice {
    void info(String message);

    void warn(String warning);

    void error(String error);
}
