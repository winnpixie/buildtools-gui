package io.github.winnpixie.btgui.ui.windows.run;

import io.github.winnpixie.btgui.tasks.BuildToolsExecutor;
import io.github.winnpixie.btgui.ui.windows.main.MainWindow;
import io.github.winnpixie.btgui.ui.windows.run.panels.OutputPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RunWindow extends JFrame {
    private final BuildToolsExecutor executor;

    public RunWindow(BuildToolsExecutor executor) throws HeadlessException {
        super("BuildTools Output");

        this.executor = executor;

        this.configureWindow();
        this.populateWithComponents();
        this.packAndDisplay();
    }

    public BuildToolsExecutor getExecutor() {
        return executor;
    }

    public void configureWindow() {
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setPreferredSize(new Dimension(480, 640));

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (executor.isActive()) {
                    RunWindow.super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    JOptionPane.showMessageDialog(RunWindow.this, "Please wait for BuildTools to finish before closing this window.");
                } else {
                    RunWindow.super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                }
            }
        });

        super.setIconImage(MainWindow.WINDOW_ICON);
    }

    public void populateWithComponents() {
        super.add(new OutputPanel(this));
    }

    public void packAndDisplay() {
        super.pack();
        super.setVisible(true);
        super.setLocationRelativeTo(null);
    }
}
