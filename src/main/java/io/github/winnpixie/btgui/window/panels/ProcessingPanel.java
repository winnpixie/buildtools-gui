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
import java.util.List;

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
        prepareBtn.setBounds(5, 5, 465, 25);
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

        runBtn.setBounds(475, 5, 465, 25);
        runBtn.addActionListener(e -> new Thread(() -> {
            BuildToolsGUI.RUNNING = true;

            runBtn.setEnabled(false);
            setOutputText("");

            File buildToolsFile = new File(BuildToolsGUI.CURRENT_DIRECTORY, "BuildTools.jar");
            if (ProgramOptions.downloadBuildTools) {
                appendToLog("Downloading BuildTools.jar from https://hub.spigotmc.org/");
                byte[] buildToolsData = downloadBuildTools();
                if (buildToolsData.length == 0) {
                    resetRunStatus();

                    appendToLog("\nCould not download BuildTools, stopping!");
                    return;
                }

                if (!saveBuildTools(buildToolsFile, buildToolsData)) {
                    resetRunStatus();

                    appendToLog("\nCould not save BuildTools.jar to storage device, stopping!");
                    return;
                }

                appendToLog("Downloaded and saved BuildTools.jar to storage device.");
            }

            appendToLog("\nCreating run directory");
            File runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, "run");
            if (ProgramOptions.isolateRuns) {
                runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, String.format("run-%d", System.currentTimeMillis()));
            }

            if (!runDir.exists() && !runDir.mkdir()) {
                resetRunStatus();

                appendToLog("\nCould not create run directory, stopping!");
                return;
            }

            appendToLog(String.format("Created run directory @ %s", runDir.getPath()));

            appendToLog("\nCopying BuildTools.jar to run directory");
            if (!copyBuildToolsToRunDirectory(buildToolsFile, runDir)) {
                resetRunStatus();

                appendToLog("Could not copy BuildTools.jar to run directory!");
                return;
            }

            appendToLog("\nJava Command:");
            String[] javaCommand = ProgramOptions.buildJavaCommand();
            appendToLog(String.join(" ", javaCommand));

            appendToLog("\nBuildTools Arguments:");
            List<String> buildToolsArguments = BuildToolsOptions.buildArguments();
            appendToLog(String.join(" ", buildToolsArguments));

            appendToLog("\nFull Command:");
            ProcessBuilder buildToolsProcessBuilder = buildProcess(javaCommand, runDir, buildToolsArguments);
            appendToLog(String.join(" ", buildToolsProcessBuilder.command()));

            appendToLog("\nPlease wait for BuildTools to finish...");
            try {
                Process buildToolsProcess = buildToolsProcessBuilder.start();

                try (BufferedReader br = new BufferedReader(new InputStreamReader(buildToolsProcess.getInputStream()))) {
                    br.lines().forEach(this::appendToLog);
                }

                buildToolsProcess.waitFor();
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                // (optionally) Clean up after ourselves :)
                if (ProgramOptions.deleteRunOnFinish) {
                    appendToLog("\nCleaning up...");

                    try {
                        IOHelper.delete(runDir);

                        appendToLog("Finished.");
                    } catch (IOException ex) {
                        ex.printStackTrace();

                        appendToLog("\nCould not delete run directory.");
                    }
                }

                resetRunStatus();

                appendToLog("\nCompleted (hopefully) without errors!");
            }
        }).start());
        super.add(runBtn);

        outputField.setBounds(5, 35, 935, 530);
        outputField.setEditable(false);
        DefaultCaret caret = (DefaultCaret) outputField.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPane = new JScrollPane(outputField);
        scrollPane.setBounds(outputField.getBounds());
        scrollPane.setAutoscrolls(true);
        super.add(scrollPane);
    }

    private void resetRunStatus() {
        BuildToolsGUI.RUNNING = false;

        runBtn.setEnabled(true);
    }

    private byte[] downloadBuildTools() {
        try {
            return IOHelper.getBytes("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    private boolean saveBuildTools(File buildToolsFile, byte[] binData) {
        try {
            Files.write(buildToolsFile.toPath(), binData);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean copyBuildToolsToRunDirectory(File originalBuildTools, File runDirectory) {
        try {
            File buildToolsCopy = new File(runDirectory, "BuildTools.jar");
            Files.copy(originalBuildTools.toPath(), buildToolsCopy.toPath());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private ProcessBuilder buildProcess(String[] javaCommand, File workingDirectory, List<String> buildToolsArguments) {
        ProcessBuilder processBuilder = new ProcessBuilder()
                .command(javaCommand)
                .directory(workingDirectory)
                .redirectErrorStream(true);
        processBuilder.command().addAll(buildToolsArguments);
        processBuilder.environment().put("JAVA_HOME", ProgramOptions.javaHome);

        return processBuilder;
    }
}
