package io.github.winnpixie.btgui.utilities;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public final class SwingHelper {
    private static final HyperlinkListener LINK_LISTENER = event -> {
        if (event.getEventType() != HyperlinkEvent.EventType.ACTIVATED) return;

        SystemHelper.openLink(event.getURL());
    };

    private SwingHelper() {
    }

    public static JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = createLabel(text);
        label.setBounds(x, y, width, height);

        return label;
    }

    public static JLabel createLabel(String text) {
        return new JLabel(asHTML(text));
    }

    public static JEditorPane createHyperlinkedLabel(String text, int x, int y, int width, int height) {
        JEditorPane editorPane = createHyperlinkedLabel(text);
        editorPane.setBounds(x, y, width, height);

        return editorPane;
    }

    public static JEditorPane createHyperlinkedLabel(String text) {
        JEditorPane editorPane = new JEditorPane("text/html", asHTML(text));
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(LINK_LISTENER);

        return editorPane;
    }

    public static void setTooltip(JComponent component, String text) {
        component.setToolTipText(asHTML(text));
    }

    private static String asHTML(String text) {
        return String.format("<html>%s</html>", text
                .replace("\n", "<br />"));
    }
}
