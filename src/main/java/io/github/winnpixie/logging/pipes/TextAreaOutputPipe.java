package io.github.winnpixie.logging.pipes;

import io.github.winnpixie.logging.LogItem;
import io.github.winnpixie.logging.OutputPipe;

import javax.swing.*;

public class TextAreaOutputPipe implements OutputPipe {
    private final JTextArea textArea;

    public TextAreaOutputPipe(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void print(LogItem item) {
        textArea.append(String.format("%s%n", item.formattedMessage(true)));

        // Scroll to the end of the document
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}