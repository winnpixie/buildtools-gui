package io.github.winnpixie.btgui.utilities;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class SwingHelper {
    private static final HyperlinkListener linkListener = e -> {
        if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) return;

        try {
            Desktop.getDesktop().browse(e.getURL().toURI());
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    };

    public static JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(String.format("<html>%s</html>", text));
        label.setBounds(x, y, width, height);

        return label;
    }

    public static JEditorPane createHyperlinkedLabel(String text, int x, int y, int width, int height) {
        JEditorPane editorPane = new JEditorPane("text/html", String.format("<html>%s</html>", text));
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(linkListener);
        editorPane.setBounds(x, y, width, height);

        return editorPane;
    }

    public static void setTooltip(JComponent component, String text) {
        text = text.replace("\n", "<br />");

        component.setToolTipText(String.format("<html>%s</html>", text));
    }
}
