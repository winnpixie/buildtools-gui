package io.github.winnpixie.logging.outputs;

import io.github.winnpixie.logging.OutputDevice;

import javax.swing.*;

public class TextAreaOutputDevice implements OutputDevice {
    private final JTextArea textArea;

    public TextAreaOutputDevice(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void print(String message) {
        textArea.append(String.format("%s%n", message));

        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
