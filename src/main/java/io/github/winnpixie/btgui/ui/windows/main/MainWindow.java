package io.github.winnpixie.btgui.ui.windows.main;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.ui.windows.main.panels.AboutPanel;
import io.github.winnpixie.btgui.ui.windows.main.panels.BuildToolsOptionsPanel;
import io.github.winnpixie.btgui.ui.windows.main.panels.ProcessingPanel;
import io.github.winnpixie.btgui.ui.windows.main.panels.ProgramOptionsPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

public class MainWindow extends JFrame {
    public static Image WINDOW_ICON;

    private final int windowWidth;
    private final int windowHeight;

    public MainWindow(int width, int height) throws HeadlessException {
        super(String.format("BuildTools GUI (v%s) - An unofficial BuildTools Frontend", BuildToolsGUI.VERSION));

        this.windowWidth = width;
        this.windowHeight = height;

        this.configureWindow();
        this.populateWithComponents();
        this.packAndDisplay();
    }

    public void configureWindow() {
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setPreferredSize(new Dimension(windowWidth, windowHeight));

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (BuildToolsGUI.getActiveBuilds() > 0) {
                    MainWindow.super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    JOptionPane.showMessageDialog(MainWindow.this, "Please wait for ALL BuildTools tasks to finish before exiting the program.");
                } else {
                    MainWindow.super.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            }
        });

        try (InputStream is = this.getClass().getResourceAsStream("/bt-gui-SMALL.png")) {
            if (is != null) {
                super.setIconImage(WINDOW_ICON = ImageIO.read(is));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateWithComponents() {
        JTabbedPane tabsPane = new JTabbedPane();

        tabsPane.add("Program Options", new ProgramOptionsPanel());
        tabsPane.add("BuildTools Options", new BuildToolsOptionsPanel());
        tabsPane.add("Output", new ProcessingPanel());
        tabsPane.add("About", new AboutPanel());

        super.add(tabsPane);
    }

    public void packAndDisplay() {
        super.pack();
        super.setVisible(true);
        super.setLocationRelativeTo(null);
    }
}
