package io.github.winnpixie.btgui.ui.components;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

// https://stackoverflow.com/a/16229082
public class SOTextField extends JTextField {
    private String placeholder;

    public SOTextField(String placeholder) {
        this.placeholder = placeholder;
    }

    public SOTextField(String text, String placeholder) {
        super(text);

        this.placeholder = placeholder;
    }

    public SOTextField(int columns, String placeholder) {
        super(columns);

        this.placeholder = placeholder;
    }

    public SOTextField(String text, int columns, String placeholder) {
        super(text, columns);

        this.placeholder = placeholder;
    }

    public SOTextField(Document doc, String text, int columns, String placeholder) {
        super(doc, text, columns);

        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!getText().isEmpty()) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(super.getDisabledTextColor());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawString(placeholder, super.getInsets().left, super.getInsets().top + g.getFontMetrics().getMaxAscent());
    }
}
