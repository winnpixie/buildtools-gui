package io.github.winnpixie.btgui.ui;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.ui.panels.AboutPanel;
import io.github.winnpixie.btgui.ui.panels.ConfigurationTabsPanel;
import io.github.winnpixie.btgui.ui.panels.JobsPanel;
import io.github.winnpixie.logging.LogLevel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

public class MainWindow extends JFrame {
    public MainWindow(int width, int height) throws HeadlessException {
        super("BuildTools GUI");

        this.configureWindow(width, height);
        this.populateWithComponents();
        this.packAndDisplay();

        try (InputStream iconStream = getClass().getResourceAsStream("/assets/icon-small.png")) {
            super.setIconImage(ImageIO.read(iconStream));
        } catch (IOException exception) {
            BuildToolsGUI.LOGGER.log(LogLevel.WARNING, exception, "Issue setting window icon");
        }
    }

    public void configureWindow(int width, int height) {
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setPreferredSize(new Dimension(width, height));

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (BuildToolsGUI.JOB_TRACKER.get() > 0) {
                    MainWindow.super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    JOptionPane.showMessageDialog(MainWindow.this, "Please wait for ALL BuildTools tasks to finish before exiting the program.");
                } else {
                    MainWindow.super.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            }
        });
    }

    public void populateWithComponents() {
        JTabbedPane panelTabs = new JTabbedPane();

        panelTabs.add("Options", new ConfigurationTabsPanel());
        panelTabs.add("Jobs", new JobsPanel());
        panelTabs.add("About", new AboutPanel());

        super.add(panelTabs);
    }

    public void packAndDisplay() {
        super.pack();
        super.setVisible(true);
        super.setLocationRelativeTo(null);
    }
}
