package io.github.winnpixie.btgui.ui.panels.workers;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.tasks.BuildToolsTask;
import io.github.winnpixie.logging.PipedLogger;
import io.github.winnpixie.logging.pipes.TextAreaOutputPipe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class WorkerPanel extends JPanel {
    private final JTextArea outputArea = new JTextArea();

    private final JTabbedPane tabController;

    private transient Thread workerThread;

    public WorkerPanel(JTabbedPane tabController) {
        super();

        this.tabController = tabController;

        this.createTask();

        super.setLayout(null);
        this.configurePanel();
        this.populateWithComponents();
    }

    private void createTask() {
        PipedLogger logger = new PipedLogger(new TextAreaOutputPipe(outputArea));

        BuildToolsTask task = new BuildToolsTask(logger,
                BuildToolsGUI.PROGRAM_CONFIGURATOR.build(),
                BuildToolsGUI.JAVA_CONFIGURATOR.build(),
                BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.build());

        this.workerThread = new Thread(() -> {
            BuildToolsGUI.JOB_TRACKER.incrementAndGet();

            int exitCode = task.getAsInt();
            if (exitCode == 0) {
                logger.info("The build process has completed successfully!");
            } else {
                logger.severe("An error occurred (code %d) while running this task.", exitCode);
            }

            BuildToolsGUI.JOB_TRACKER.decrementAndGet();
        }, "build-tools_task");

        workerThread.start();
    }

    private void configurePanel() {
        super.setFocusable(true);
        super.requestFocus();

        super.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), "Ctrl+W");
        super.getActionMap().put("Ctrl+W", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!workerThread.isAlive()) tabController.remove(WorkerPanel.this);
            }
        });
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
