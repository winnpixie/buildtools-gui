package io.github.winnpixie.btgui.window;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.window.panels.BuildToolsGUIOptionsPanel;
import io.github.winnpixie.btgui.window.panels.BuildToolsOptionsPanel;
import io.github.winnpixie.btgui.window.panels.ProcessingPanel;

import javax.swing.*;
import java.awt.*;

public class BTWindow extends JFrame {
    public BTWindow() throws HeadlessException {
        super(String.format("BuildTools GUI (v%s) - https://github.com/winnpixie/bt-gui/", BuildToolsGUI.VERSION));

        this.configureWindow();
        this.populateWithComponents();
        this.packAndDisplay();
    }

    public void configureWindow() {
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setPreferredSize(new Dimension(640, 480));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void populateWithComponents() {
        JTabbedPane tabsPane = new JTabbedPane();

        tabsPane.add("BTGUI Options", new BuildToolsGUIOptionsPanel());
        tabsPane.add("BT Options", new BuildToolsOptionsPanel());
        tabsPane.add("Processing", new ProcessingPanel());

        super.add(tabsPane);
    }

    public void packAndDisplay() {
        super.pack();
        super.setVisible(true);
        super.setLocationRelativeTo(null);
    }
}
