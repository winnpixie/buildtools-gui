package io.github.winnpixie.btgui.window;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.window.panels.BuildToolsOptionsPanel;
import io.github.winnpixie.btgui.window.panels.CreditsPanel;
import io.github.winnpixie.btgui.window.panels.ProcessingPanel;
import io.github.winnpixie.btgui.window.panels.ProgramOptionsPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

public class BTGUIWindow extends JFrame {
    public BTGUIWindow() throws HeadlessException {
        super(String.format("BuildTools GUI (v%s) - https://github.com/winnpixie/bt-gui/", BuildToolsGUI.VERSION));

        this.configureWindow();
        this.populateWithComponents();
        this.packAndDisplay();
    }

    public void configureWindow() {
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setPreferredSize(new Dimension(640, 480));

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (BuildToolsGUI.RUNNING) {
                    BTGUIWindow.super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    JOptionPane.showMessageDialog(BTGUIWindow.this, "Please wait for BuildTools to finish before closing the GUI!");
                } else {
                    BTGUIWindow.super.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            }
        });

        try (InputStream is = this.getClass().getResourceAsStream("/bt-gui-SMALL.png")) {
            if (is != null) {
                super.setIconImage(ImageIO.read(is));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void populateWithComponents() {
        JTabbedPane tabsPane = new JTabbedPane();

        tabsPane.add("Program Options", new ProgramOptionsPanel());
        tabsPane.add("BuildTools Options", new BuildToolsOptionsPanel());
        tabsPane.add("Output", new ProcessingPanel());
        tabsPane.add("Credits", new CreditsPanel());

        super.add(tabsPane);
    }

    public void packAndDisplay() {
        super.pack();
        super.setVisible(true);
        super.setLocationRelativeTo(null);
    }
}
