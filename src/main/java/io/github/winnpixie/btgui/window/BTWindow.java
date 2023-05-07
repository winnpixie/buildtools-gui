package io.github.winnpixie.btgui.window;

import io.github.winnpixie.btgui.BTGUI;
import io.github.winnpixie.btgui.window.panels.BTGUIOptionsPanel;
import io.github.winnpixie.btgui.window.panels.BTOptionsPanel;
import io.github.winnpixie.btgui.window.panels.BTOutputPanel;

import javax.swing.*;
import java.awt.*;

public class BTWindow extends JFrame {
    public BTWindow() throws HeadlessException {
        super(String.format("BuildTools GUI (v%s) - https://github.com/winnpixie/bt-gui/", BTGUI.VERSION));

        this.configureWindow();
        this.populateWithComponents();
        this.packAndDisplay();
    }

    public void configureWindow() {
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setPreferredSize(new Dimension(640, 480));

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void populateWithComponents() {
        JTabbedPane tabsPane = new JTabbedPane();

        tabsPane.add("BT Options", new BTOptionsPanel());
        tabsPane.add("BT Output", new BTOutputPanel());
        tabsPane.add("BTGUI Options", new BTGUIOptionsPanel());

        super.add(tabsPane);
    }

    public void packAndDisplay() {
        super.pack();
        super.setVisible(true);
        super.setLocationRelativeTo(null);
    }
}
