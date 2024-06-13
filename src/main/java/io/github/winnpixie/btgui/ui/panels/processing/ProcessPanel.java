package io.github.winnpixie.btgui.ui.panels.processing;

import io.github.winnpixie.btgui.tasks.BuildToolsTask;
import io.github.winnpixie.btgui.utilities.ProcessHelper;
import io.github.winnpixie.logging.CustomLogger;
import io.github.winnpixie.logging.outputs.TextAreaOutputDevice;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ProcessPanel extends JPanel {
    private final JTextArea outputArea = new JTextArea();
    private final CustomLogger logger = new CustomLogger(new TextAreaOutputDevice(outputArea));

    private final JTabbedPane tabManager;
    private final Thread taskThread;

    public ProcessPanel(JTabbedPane tabManager, BuildToolsTask task) {
        super();

        this.tabManager = tabManager;
        this.taskThread = new Thread(() -> {
            ProcessHelper.addProcess();

            task.setLogger(this.logger);
            if (task.get()) {
                logger.info("\n\nThe build process has completed successfully!");
            } else {
                logger.error("\n\nAn error occurred during the build process!");
            }

            ProcessHelper.removeProcess();
        });

        super.setLayout(null);
        this.configurePanel();
        this.populateWithComponents();

        taskThread.start();
    }

    private void configurePanel() {
        super.setFocusable(true);
        super.requestFocus();

        super.getActionMap().put("Ctrl+W", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!taskThread.isAlive()) tabManager.remove(ProcessPanel.this);
            }
        });
        super.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), "Ctrl+W");
    }

    private void populateWithComponents() {
        outputArea.setBounds(0, 1, 935, 355);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(outputArea.getBounds());
        super.add(scrollPane);
    }
}
