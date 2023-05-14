package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.config.BuildToolsOptions;
import io.github.winnpixie.btgui.config.ProgramOptions;
import io.github.winnpixie.btgui.utilities.IOHelper;
import io.github.winnpixie.btgui.window.JTextFieldWithPlaceholder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ProcessingPanel extends JPanel {
    private final JTextField commandToRun = new JTextFieldWithPlaceholder("Click \"Preview\" to preview what BuildTools will run.");
    private final JButton prepareBtn = new JButton("Preview");
    private final JButton runBtn = new JButton("Run");
    private final JTextArea outputLog = new JTextArea();

    public ProcessingPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void setOutputText(String text) {
        outputLog.setText(text);
        if (!text.isEmpty()) BuildToolsGUI.LOGGER.info(text);
    }

    private void appendToLog(String text) {
        outputLog.append(String.format("%s%n", text));
        if (text.isEmpty()) BuildToolsGUI.LOGGER.info(text);
    }

    private void populateWithComponents() {
        commandToRun.setBounds(0, 0, 400, 20);
        commandToRun.setToolTipText("The command that will run BuildTools.jar");
        commandToRun.setEditable(false);
        super.add(commandToRun);

        prepareBtn.setBounds(400, 0, 100, 20);
        prepareBtn.addActionListener(e -> commandToRun.setText(String.join(" ", BuildToolsOptions.buildArguments())));
        super.add(prepareBtn);

        runBtn.setBounds(500, 0, 100, 20);
        runBtn.addActionListener(e -> new Thread(() -> {
            setOutputText(""); // Clear output log

            // Download BuildTools from SpigotMC Jenkins
            File buildToolsFile = new File(BuildToolsGUI.CURRENT_DIRECTORY, "BuildTools.jar");
            if (ProgramOptions.downloadBuildTools) {
                try {
                    appendToLog("Downloading BuildTools.jar from https://hub.spigotmc.org/");
                    byte[] btData = IOHelper.getBytes("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar");
                    Files.write(buildToolsFile.toPath(), btData);
                } catch (IOException ex) {
                    appendToLog("Failed to download or save BuildTools.jar, short-circuiting!");

                    ex.printStackTrace();
                    return;
                }
            }

            // Create Run Directory
            File runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, "run");
            if (ProgramOptions.isolateRuns) {
                runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, String.format("run-%d", System.currentTimeMillis()));
            }

            if (!runDir.exists()) runDir.mkdir();
            appendToLog(String.format("Created run directory @ %s", runDir.getAbsolutePath()));

            try {
                appendToLog("Copying BuildTools.jar to run directory");
                // Copy BuildTools to Run directory
                File buildToolsCopy = new File(runDir, "BuildTools.jar");
                Files.copy(buildToolsFile.toPath(), buildToolsCopy.toPath());

                // Build process
                ProcessBuilder procBuilder = new ProcessBuilder()
                        .command(String.format("\"%s%cbin%cjava\"", ProgramOptions.javaHome, File.separatorChar, File.separatorChar),
                                String.format("-Xms%s", ProgramOptions.heapSize),
                                String.format("-Xmx%s", ProgramOptions.heapSize),
                                "-jar",
                                "BuildTools.jar"
                        )
                        .directory(runDir)
                        .inheritIO();
                procBuilder.command().addAll(BuildToolsOptions.buildArguments());
                procBuilder.environment().put("JAVA_HOME", ProgramOptions.javaHome);

                appendToLog("Running BuildTools.jar");
                appendToLog("BuildTools Command:");
                appendToLog(String.join(" ", BuildToolsOptions.buildArguments()));
                appendToLog("");
                appendToLog("Full Command:");
                appendToLog(String.join(" ", procBuilder.command()));
                appendToLog("Please wait...");

                // Start process and wait to finish
                Process proc = procBuilder.start();
                proc.waitFor();
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                appendToLog("Completed (hopefully) without errors!");

                // (optionally) Clean up after ourselves :)
                if (ProgramOptions.deleteRunOnFinish) {
                    appendToLog("Cleaning up...");
                    IOHelper.delete(runDir);
                }
            }
        }).start());
        super.add(runBtn);

        outputLog.setBounds(0, 20, 620, 420);
        outputLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputLog);
        scrollPane.setBounds(outputLog.getBounds());
        super.add(scrollPane);
    }
}
