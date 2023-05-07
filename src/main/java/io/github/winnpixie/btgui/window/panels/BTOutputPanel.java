package io.github.winnpixie.btgui.window.panels;

import javax.swing.*;

public class BTOutputPanel extends JPanel {
    private final JTextArea outputLog = new JTextArea();

    public BTOutputPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        outputLog.setBounds(0, 0, 620, 440);
        outputLog.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputLog);
        scrollPane.setBounds(outputLog.getBounds());
        super.add(scrollPane);
    }
}
