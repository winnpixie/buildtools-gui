package io.github.winnpixie.btgui.ui.windows.main.panels;

import io.github.winnpixie.btgui.config.BuildToolsOptions;
import io.github.winnpixie.btgui.config.ProgramOptions;
import io.github.winnpixie.btgui.tasks.BuildToolsExecutor;
import io.github.winnpixie.btgui.ui.windows.run.RunWindow;

import javax.swing.*;

public class ProcessingPanel extends JPanel {
    private final JButton previewBtn = new JButton("Preview Command");
    private final JButton runBtn = new JButton("Run BuildTools");
    private final JTextArea outputArea = new JTextArea();

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
            outputArea.setText("BuildTools Command (PREVIEW)\n");

            outputArea.append("\nJava Command:\n");
            outputArea.append(javaCommand);

            outputArea.append("\n\nBuildTools Arguments:\n");
            outputArea.append(buildToolsArgs);

            outputArea.append("\n\nFull Command:\n");
            outputArea.append(String.format("%s %s", javaCommand, buildToolsArgs));
        });
        super.add(previewBtn);

        runBtn.setBounds(475, 5, 465, 25);
        runBtn.addActionListener(e -> {
            outputArea.setText("Running BuildTools!");

            new RunWindow(new BuildToolsExecutor(ProgramOptions.buildJavaCommand(),
                    BuildToolsOptions.buildArguments()));
        });
        super.add(runBtn);

        outputArea.setBounds(5, 35, 935, 530);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(outputArea.getBounds());
        super.add(scrollPane);
    }
}
