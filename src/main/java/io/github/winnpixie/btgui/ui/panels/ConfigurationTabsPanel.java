package io.github.winnpixie.btgui.ui.panels;

import io.github.winnpixie.btgui.ui.panels.configuration.BuildToolsConfigurationPanel;
import io.github.winnpixie.btgui.ui.panels.configuration.JavaConfigurationPanel;
import io.github.winnpixie.btgui.ui.panels.configuration.ProgramConfigurationPanel;

import javax.swing.*;
import java.awt.*;

public class ConfigurationTabsPanel extends JPanel {
    public ConfigurationTabsPanel() {
        super();

        super.setLayout(new GridLayout(1, 1, 0, 0));
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.add("BuildTools", new BuildToolsConfigurationPanel());
        tabs.add("Java", new JavaConfigurationPanel());
        tabs.add("Program", new ProgramConfigurationPanel());

        super.add(tabs);
    }
}
