package io.github.winnpixie.btgui.ui;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.ui.panels.AboutPanel;
import io.github.winnpixie.btgui.ui.panels.ConfigurationTabsPanel;
import io.github.winnpixie.btgui.ui.panels.JobsPanel;
import io.github.winnpixie.btgui.utilities.IOHelper;
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

        try (InputStream iconStream = IOHelper.getResourceStream("/assets/icon-small.png")) {
            super.setIconImage(ImageIO.read(iconStream));
        } catch (IOException ioe) {
            BuildToolsGUI.LOGGER.log(LogLevel.WARNING, ioe, "Issue setting window icon");
        }
    }

    public void configureWindow(int width, int height) {
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setPreferredSize(new Dimension(width, height));

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (BuildToolsGUI.JOB_TRACKER.get() == 0) {
                    MainWindow.super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    return;
                }

                MainWindow.super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                JOptionPane.showMessageDialog(MainWindow.this,
                        "You must wait for all active workers to finish before exiting.",
                        "Whoops!", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public void populateWithComponents() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Options", new ConfigurationTabsPanel());
        tabs.add("Jobs", new JobsPanel());
        tabs.add("About", new AboutPanel());

        super.add(tabs);
    }

    public void packAndDisplay() {
        super.pack();
        super.setVisible(true);
        super.setLocationRelativeTo(null);
    }
}
