package io.github.winnpixie.btgui.ui.panels.workers;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.tasks.BuildToolsTask;
import io.github.winnpixie.logging.LogLevel;
import io.github.winnpixie.logging.PipedLogger;
import io.github.winnpixie.logging.pipes.TextAreaOutputPipe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class WorkerPanel extends JPanel {
    private final JTextArea outputArea = new JTextArea();

    private final JTabbedPane tabController;

    private transient BuildToolsTask task;
    private transient CompletableFuture<Integer> worker;

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

        this.task = new BuildToolsTask(logger,
                BuildToolsGUI.PROGRAM_CONFIGURATOR.build(),
                BuildToolsGUI.JAVA_CONFIGURATOR.build(),
                BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.build());

        BuildToolsGUI.JOB_TRACKER.incrementAndGet();
        this.worker = CompletableFuture.supplyAsync(() -> {
                    try {
                        return task.getAsInt();
                    } catch (Exception unknown) {
                        throw new CompletionException(unknown);
                    }
                }, BuildToolsGUI.WORKER_SERVICE)
                .whenComplete((exitCode, throwable) -> {
                    BuildToolsGUI.JOB_TRACKER.decrementAndGet();

                    if (throwable != null) {
                        logger.log(LogLevel.SEVERE, throwable, "Uncaught error while running");
                        return;
                    }

                    if (exitCode == 0) {
                        logger.info("The build process has completed successfully!");
                    } else {
                        logger.severe("The worker encountered an error (code %d) while executing.", exitCode);
                    }
                });
    }

    private void configurePanel() {
        super.setFocusable(true);
        super.requestFocus();

        super.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), "Ctrl+W");
        super.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK), "Ctrl+D");
        super.getActionMap().put("Ctrl+W", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!worker.isDone()) {
                    JOptionPane.showMessageDialog(WorkerPanel.this,
                            "You are not permitted to close the panel of an active worker!",
                            "Whoops!", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                tabController.remove(WorkerPanel.this);
            }
        });

        super.getActionMap().put("Ctrl+D", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                task.terminate();
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
