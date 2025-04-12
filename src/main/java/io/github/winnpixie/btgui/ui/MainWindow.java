package io.github.winnpixie.btgui.ui;

import io.github.winnpixie.btgui.ui.panels.AboutPanel;
import io.github.winnpixie.btgui.ui.panels.OptionsPanel;
import io.github.winnpixie.btgui.ui.panels.ProcessingPanel;
import io.github.winnpixie.btgui.utilities.ProcessHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

public class MainWindow extends JFrame {
    public static Image WINDOW_ICON;

    public MainWindow(int width, int height) throws HeadlessException {
        super("BuildTools GUI");

        this.configureWindow(width, height);
        this.populateWithComponents();
        this.packAndDisplay();
    }

    public void configureWindow(int width, int height) {
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setPreferredSize(new Dimension(width, height));

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (ProcessHelper.PROCESS_COUNTER.get() > 0) {
                    MainWindow.super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    JOptionPane.showMessageDialog(MainWindow.this, "Please wait for ALL BuildTools tasks to finish before exiting the program.");
                } else {
                    MainWindow.super.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            }
        });

        try (InputStream is = this.getClass().getResourceAsStream("/assets/icon-small.png")) {
            if (is != null) super.setIconImage(WINDOW_ICON = ImageIO.read(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateWithComponents() {
        JTabbedPane panelTabs = new JTabbedPane();

        panelTabs.add("Options", new OptionsPanel());
        panelTabs.add("Processing", new ProcessingPanel());
        panelTabs.add("About", new AboutPanel());

        super.add(panelTabs);
    }

    public void packAndDisplay() {
        super.pack();
        super.setVisible(true);
        super.setLocationRelativeTo(null);
    }
}
