package io.github.winnpixie.btgui.utilities;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class SwingHelper {
    private static final HyperlinkListener linkListener = e -> {
        if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) return;

        SystemHelper.openLink(e.getURL());
    };

    public static JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = createLabel(text);
        label.setBounds(x, y, width, height);

        return label;
    }

    public static JLabel createLabel(String text) {
        return new JLabel(String.format("<html>%s</html>", text));
    }

    public static JEditorPane createHyperlinkedLabel(String text, int x, int y, int width, int height) {
        JEditorPane editorPane = createHyperlinkedLabel(text);
        editorPane.setBounds(x, y, width, height);

        return editorPane;
    }

    public static JEditorPane createHyperlinkedLabel(String text) {
        JEditorPane editorPane = new JEditorPane("text/html", String.format("<html>%s</html>", text));
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(linkListener);

        return editorPane;
    }

    public static void setTooltip(JComponent component, String text) {
        text = text.replace("\n", "<br />");

        component.setToolTipText(String.format("<html>%s</html>", text));
    }
}
