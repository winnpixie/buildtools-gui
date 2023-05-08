package io.github.winnpixie.btgui.window.panels;

import javax.swing.*;

public class BuildToolsGUIOptionsPanel extends JPanel {
    private final JCheckBox downloadBuildToolsOption = new JCheckBox("Download BuildTools JAR", true);

    public BuildToolsGUIOptionsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        downloadBuildToolsOption.setBounds(0, 0, 200, 20);
        super.add(downloadBuildToolsOption);
    }
}
