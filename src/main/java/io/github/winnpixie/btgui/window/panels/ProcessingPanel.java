package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.config.BuildToolsOptions;
import io.github.winnpixie.btgui.config.ProgramOptions;
import io.github.winnpixie.btgui.utilities.IOHelper;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class ProcessingPanel extends JPanel {
    private final JButton prepareBtn = new JButton("Preview Command");
    private final JButton runBtn = new JButton("Run BuildTools");
    private final JTextArea outputField = new JTextArea();

    public ProcessingPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void setOutputText(String text) {
        outputField.setText(text);
        if (!text.trim().isEmpty()) BuildToolsGUI.LOGGER.info(text);
    }

    private void appendToLog(String text) {
        outputField.append(String.format("%s%n", text));
        if (!text.trim().isEmpty()) BuildToolsGUI.LOGGER.info(text);
    }

    private void populateWithComponents() {
        prepareBtn.setBounds(0, 0, 300, 20);
        prepareBtn.addActionListener(e -> {
            String javaCommand = String.join(" ", ProgramOptions.buildJavaCommand());
            String buildToolsArgs = String.join(" ", BuildToolsOptions.buildArguments());
            setOutputText("Java Command:\n");
            appendToLog(javaCommand);

            appendToLog("\nBuildTools Arguments:");
            appendToLog(buildToolsArgs);

            appendToLog("\nFull Command:");
            appendToLog(String.format("%s %s", javaCommand, buildToolsArgs));
        });
        super.add(prepareBtn);

        runBtn.setBounds(300, 0, 300, 20);
        runBtn.addActionListener(e -> new Thread(() -> {
            BuildToolsGUI.RUNNING = true;

            runBtn.setEnabled(false);
            setOutputText("");

            File buildToolsFile = new File(BuildToolsGUI.CURRENT_DIRECTORY, "BuildTools.jar");
            if (ProgramOptions.downloadBuildTools) {
                try {
                    appendToLog("Downloading BuildTools.jar from https://hub.spigotmc.org/");
                    byte[] btData = IOHelper.getBytes("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar");
                    Files.write(buildToolsFile.toPath(), btData);
                } catch (IOException ex) {
                    BuildToolsGUI.RUNNING = false;

                    runBtn.setEnabled(true);
                    appendToLog("Failed to download or save BuildTools.jar, short-circuiting!");

                    ex.printStackTrace();
                    return;
                }
            }

            File runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, "run");
            if (ProgramOptions.isolateRuns) {
                runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, String.format("run-%d", System.currentTimeMillis()));
            }

            if (!runDir.exists()) runDir.mkdir();
            appendToLog(String.format("Created run directory @ %s", runDir.getAbsolutePath()));

            appendToLog("Copying BuildTools.jar to run directory");
            try {
                File buildToolsCopy = new File(runDir, "BuildTools.jar");
                Files.copy(buildToolsFile.toPath(), buildToolsCopy.toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            appendToLog("\nJava Command:");
            appendToLog(String.join(" ", ProgramOptions.buildJavaCommand()));

            appendToLog("\nBuildTools Arguments:");
            appendToLog(String.join(" ", BuildToolsOptions.buildArguments()));
            try {

                ProcessBuilder btProcBuilder = new ProcessBuilder()
                        .command(ProgramOptions.buildJavaCommand())
                        .directory(runDir);
                btProcBuilder.command().addAll(BuildToolsOptions.buildArguments());
                btProcBuilder.environment().put("JAVA_HOME", ProgramOptions.javaHome);

                appendToLog("\nFull Command:");
                appendToLog(String.join(" ", btProcBuilder.command()));

                appendToLog("\nPlease wait for BuildTools to finish...\n");
                Process btProcess = btProcBuilder.start();

                try (BufferedReader br = new BufferedReader(new InputStreamReader(btProcess.getInputStream()))) {
                    br.lines().forEach(this::appendToLog);
                }

                btProcess.waitFor();
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                BuildToolsGUI.RUNNING = false;

                appendToLog("Completed (hopefully) without errors!");
                runBtn.setEnabled(true);

                // (optionally) Clean up after ourselves :)
                if (ProgramOptions.deleteRunOnFinish) {
                    appendToLog("Cleaning up...");
                    IOHelper.delete(runDir);
                }
            }
        }).start());
        super.add(runBtn);

        outputField.setBounds(0, 20, 620, 420);
        outputField.setEditable(false);
        DefaultCaret caret = (DefaultCaret) outputField.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPane = new JScrollPane(outputField);
        scrollPane.setBounds(outputField.getBounds());
        scrollPane.setAutoscrolls(true);
        super.add(scrollPane);
    }
}
