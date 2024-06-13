package io.github.winnpixie.btgui.ui.panels;

import io.github.winnpixie.btgui.ui.panels.options.BuildToolsOptionsPanel;
import io.github.winnpixie.btgui.ui.panels.options.ProgramOptionsPanel;

import javax.swing.*;
import java.awt.*;

public class OptionsPanel extends JPanel {
    public OptionsPanel() {
        super();

        super.setLayout(new GridLayout(1, 1, 0, 0));
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        JTabbedPane optionTabs = new JTabbedPane();

        optionTabs.add("BuildTools", new BuildToolsOptionsPanel());
        optionTabs.add("Program", new ProgramOptionsPanel());

        super.add(optionTabs);
    }
}
