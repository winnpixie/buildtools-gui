package io.github.winnpixie.btgui.utilities;

import javax.swing.*;

public class SwingHelper {
    public static JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(String.format("<html>%s</html>", text));
        label.setBounds(x, y, width, height);

        return label;
    }

    public static void setTooltip(JComponent component, String text) {
        text = text.replace("\n", "<br />");

        component.setToolTipText(String.format("<html>%s</html>", text));
    }
}
