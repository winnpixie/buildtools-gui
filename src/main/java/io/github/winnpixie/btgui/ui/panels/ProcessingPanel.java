package io.github.winnpixie.btgui.ui.panels;

import io.github.winnpixie.btgui.config.BuildToolsOptions;
import io.github.winnpixie.btgui.config.ProgramOptions;
import io.github.winnpixie.btgui.ui.panels.processing.ProcessInfoPanel;
import io.github.winnpixie.btgui.ui.panels.processing.ProcessPanel;
import io.github.winnpixie.btgui.utilities.ProcessHelper;

import javax.swing.*;

public class ProcessingPanel extends JPanel {
    private final JButton previewBtn = new JButton("Preview Command");
    private final JButton runBtn = new JButton("Run BuildTools");
    private final JTextArea previewArea = new JTextArea();
    private final JTabbedPane processTabs = new JTabbedPane();

    public ProcessingPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        previewBtn.setBounds(5, 5, 465, 25);
        previewBtn.addActionListener(e -> {
            String javaCommand = String.join(" ", ProgramOptions.buildJavaCommand());
            String buildToolsArgs = String.join(" ", BuildToolsOptions.buildArguments());
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
            processTabs.add(String.format("Run #%d", ProcessHelper.getProcessCount() + 1),
                    new ProcessPanel(processTabs, ProcessHelper.createProcess(
                            ProgramOptions.buildJavaCommand(),
                            BuildToolsOptions.buildArguments())));
            processTabs.setSelectedIndex(processTabs.getTabCount() - 1);
        });
        super.add(runBtn);

        previewArea.setBounds(5, 35, 935, 130);
        previewArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(previewArea);
        scrollPane.setBounds(previewArea.getBounds());
        super.add(scrollPane);

        processTabs.setBounds(5, 170, 935, 390);
        processTabs.add("Info", new ProcessInfoPanel());
        super.add(processTabs);
    }
}
