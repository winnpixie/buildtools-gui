package io.github.winnpixie.btgui.ui.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.ui.panels.workers.WorkerInfoPanel;
import io.github.winnpixie.btgui.ui.panels.workers.WorkerPanel;

import javax.swing.*;

public class JobsPanel extends JPanel {
    private final JButton previewBtn = new JButton("Preview Command");
    private final JButton runBtn = new JButton("Start BuildTools Worker");
    private final JTextArea previewArea = new JTextArea();
    private final JTabbedPane workerTabs = new JTabbedPane();

    private int nextWorkerId = 1;

    public JobsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        previewBtn.setBounds(5, 5, 465, 25);
        previewBtn.addActionListener(e -> {
            String javaCommand = String.join(" ", BuildToolsGUI.JAVA_CONFIGURATOR.build().toList());
            String buildToolsArgs = String.join(" ", BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.build().toList());
            previewArea.setText("Java Command:\n");
            previewArea.append(javaCommand);

            previewArea.append("\n\nBuildTools Arguments:\n");
            previewArea.append(buildToolsArgs);

            previewArea.append("\n\nFull Command:\n");
            previewArea.append(String.format("%s %s", javaCommand, buildToolsArgs));
        });
        super.add(previewBtn);

        runBtn.setBounds(475, 5, 465, 25);
        runBtn.addActionListener(e -> {
            previewBtn.doClick();

            workerTabs.add(String.format("Worker #%d", nextWorkerId++), new WorkerPanel(workerTabs));
            workerTabs.setSelectedIndex(workerTabs.getTabCount() - 1);
        });
        super.add(runBtn);

        previewArea.setBounds(5, 35, 935, 130);
        previewArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(previewArea);
        scrollPane.setBounds(previewArea.getBounds());
        super.add(scrollPane);

        workerTabs.setBounds(5, 170, 935, 390);
        workerTabs.add("Info", new WorkerInfoPanel());
        super.add(workerTabs);
    }
}
