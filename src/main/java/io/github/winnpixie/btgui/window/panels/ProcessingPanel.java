package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.config.BuildToolsOptions;
import io.github.winnpixie.btgui.config.ProgramOptions;
import io.github.winnpixie.btgui.utilities.IOHelper;
import io.github.winnpixie.btgui.utilities.JARHelper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Map;

public class ProcessingPanel extends JPanel {
    private final JTextField commandToRun = new JTextField();
    private final JButton prepareBtn = new JButton("Prepare");
    private final JButton runBtn = new JButton("Run");
    private final JTextArea outputLog = new JTextArea();

    public ProcessingPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void appendToOutput(String msg) {
        outputLog.append(msg);
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
        runBtn.addActionListener(e -> {
            File buildToolsFile = new File(BuildToolsGUI.CURRENT_DIRECTORY, "BuildTools.jar");
            if (ProgramOptions.downloadBuildTools) {
                try {
                    BuildToolsGUI.LOGGER.info("Downloading BuildTools.jar");
                    appendToOutput("Downloading BuildTools.jar from https://hub.spigotmc.org/");
                    byte[] btData = IOHelper.getBytes("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar");
                    Files.write(buildToolsFile.toPath(), btData);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
            }

            File runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, "run");
            if (ProgramOptions.isolateRuns) {
                runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, String.format("run-%d", System.currentTimeMillis()));
            }

            if (!runDir.exists()) runDir.mkdir();

            try {

                File buildToolsCopy = new File(runDir, "BuildTools.jar");
                Files.copy(buildToolsFile.toPath(), buildToolsCopy.toPath());

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

                Process proc = procBuilder.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (ProgramOptions.deleteRunOnFinish) {
                IOHelper.delete(runDir);
            }

            // TODO: Remove loading BT directly into this JVM, add functionality to run from a specified JAVA_HOME
            if (1 == 1) return; // Short-circuited for testing
            try {
                BuildToolsGUI.LOGGER.info("Loading BuildTools classes");
                appendToOutput("Loading BuildTools into current JVM...");

                Map<String, Class<?>> classes = JARHelper.loadClasses(buildToolsFile);

                try {
                    BuildToolsGUI.LOGGER.info("Running BuildTools");
                    classes.get("org.spigotmc.builder.Bootstrap").getDeclaredMethod("main", String[].class)
                            .invoke(null, new Object[]{BuildToolsOptions.buildArguments().toArray(new String[0])});
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        super.add(runBtn);

        outputLog.setBounds(0, 20, 620, 420);
        outputLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputLog);
        scrollPane.setBounds(outputLog.getBounds());
        super.add(scrollPane);
    }
}
