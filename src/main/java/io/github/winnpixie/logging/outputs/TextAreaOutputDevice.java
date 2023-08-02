package io.github.winnpixie.logging.outputs;

import io.github.winnpixie.logging.OutputDevice;

import javax.swing.*;

public class TextAreaOutputDevice implements OutputDevice {
    private final JTextArea textArea;

    public TextAreaOutputDevice(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void info(String message) {
        textArea.append(String.format("[INFO] %s%n", message));
    }

    @Override
    public void warn(String warning) {
        textArea.append(String.format("[WARNING] %s%n", warning));
    }

    @Override
    public void error(String error) {
        textArea.append(String.format("[ERROR] %s%n", error));
    }
}
