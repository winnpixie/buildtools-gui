package io.github.winnpixie.btgui.window;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

public class JTextFieldWithPlaceholder extends JTextField {
    private String placeholder;

    public JTextFieldWithPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public JTextFieldWithPlaceholder(String text, String placeholder) {
        super(text);
        this.placeholder = placeholder;
    }

    public JTextFieldWithPlaceholder(int columns, String placeholder) {
        super(columns);
        this.placeholder = placeholder;
    }

    public JTextFieldWithPlaceholder(String text, int columns, String placeholder) {
        super(text, columns);
        this.placeholder = placeholder;
    }

    public JTextFieldWithPlaceholder(Document doc, String text, int columns, String placeholder) {
        super(doc, text, columns);
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        if (getText().length() < 1) {
            // https://stackoverflow.com/a/16229082
            g2d.setColor(super.getDisabledTextColor());
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.drawString(placeholder, super.getInsets().left, super.getInsets().top + g.getFontMetrics().getMaxAscent());
        }
    }
}
