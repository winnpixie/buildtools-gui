package io.github.winnpixie.btgui.ui.windows.main.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.config.ProgramOptions;
import io.github.winnpixie.btgui.ui.components.SOTextField;
import io.github.winnpixie.btgui.utilities.OSHelper;
import io.github.winnpixie.btgui.utilities.SwingHelper;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ProgramOptionsPanel extends JPanel {
    private final JCheckBox downloadBuildToolsOption = new JCheckBox("Download BuildTools JAR", true);
    private final JCheckBox isolateRunsOption = new JCheckBox("Isolate Runs", true);
    private final JCheckBox openOutputDirOnFinishOption = new JCheckBox("Open Output Directory on Finish", true);
    private final JCheckBox deleteWorkDirOnFinishButton = new JCheckBox("Delete Run on Finish", false);

    private final JTextField javaHomeField = new SOTextField(ProgramOptions.javaHome, "JAVA_HOME");
    private final JButton selectJavaHomeButton = new JButton("Select Folder");

    private final JTextField jvmArgsField = new SOTextField(ProgramOptions.jvmArguments, "JVM Arguments");

    private final JTextField mavenOptsField = new SOTextField(ProgramOptions.mavenOptions, "MAVEN_OPTS");


    public ProgramOptionsPanel() {
        super();

        super.setLayout(null); // https://maven.apache.org/configure.html
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        // Download BuildTools
        downloadBuildToolsOption.setBounds(5, 0, 200, 25);
        SwingHelper.setTooltip(downloadBuildToolsOption, "Download's BuildTools.jar from SpigotMC Jenkins.");
        downloadBuildToolsOption.addActionListener(e -> ProgramOptions.downloadBuildTools = downloadBuildToolsOption.isSelected());
        super.add(downloadBuildToolsOption);

        // Isolate Runs
        isolateRunsOption.setBounds(5, 25, 200, 25);
        SwingHelper.setTooltip(isolateRunsOption, "Creates a new directory with a random name when BuildTools is ran," +
                "\nuseful for building different Minecraft versions.");
        isolateRunsOption.addActionListener(e -> ProgramOptions.isolateRuns = isolateRunsOption.isSelected());
        super.add(isolateRunsOption);

        // Open Output Directory on Finish
        openOutputDirOnFinishOption.setBounds(5, 50, 200, 25);
        SwingHelper.setTooltip(openOutputDirOnFinishOption, "Opens the BuildTools output directory when it is completed.");
        openOutputDirOnFinishOption.addActionListener(e -> ProgramOptions.openOutputAfterFinish = openOutputDirOnFinishOption.isSelected());
        super.add(openOutputDirOnFinishOption);

        // Delete Run Directory on Finish
        deleteWorkDirOnFinishButton.setBounds(5, 75, 200, 25);
        SwingHelper.setTooltip(deleteWorkDirOnFinishButton, "Deletes the BuildTools run directory when it is completed.");
        deleteWorkDirOnFinishButton.addActionListener(e -> ProgramOptions.deleteWorkDirOnFinish = deleteWorkDirOnFinishButton.isSelected());
        super.add(deleteWorkDirOnFinishButton);

        super.add(SwingHelper.createLabel("JAVA_HOME Path", 5, 125, 200, 25));
        // JAVA_HOME
        javaHomeField.setBounds(5, 150, 505, 25);
        SwingHelper.setTooltip(javaHomeField, "The directory of the JDK to run BuildTools with.");
        super.add(javaHomeField);

        // Set JAVA_HOME
        selectJavaHomeButton.setBounds(515, 150, 100, 25);
        selectJavaHomeButton.addActionListener(e -> setAndDisplayRuntime());
        super.add(selectJavaHomeButton);

        super.add(SwingHelper.createLabel("JVM Arguments", 5, 200, 200, 25));
        // JVM Arguments
        jvmArgsField.setBounds(5, 225, 610, 25);
        SwingHelper.setTooltip(jvmArgsField, "Examples: -Xms512M, -Xmx1024M, etc...\n" +
                "Do NOT comma-separate arguments, only spaces.\n" +
                "This WILL lead to unintended if an argument includes spaces, sorry!");
        jvmArgsField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ProgramOptions.jvmArguments = jvmArgsField.getText();
            }
        });
        super.add(jvmArgsField);

        super.add(SwingHelper.createLabel("MAVEN_OPTS Arguments", 5, 275, 200, 25));
        // MAVEN_OPTS
        mavenOptsField.setBounds(5, 300, 610, 25);
        SwingHelper.setTooltip(mavenOptsField, "Similar to JVM Arguments, but for Maven.\n" +
                "Examples: -Xms512M, -Xmx1024M, etc...\n" +
                "Do NOT comma-separate arguments, only spaces.\n" +
                "This WILL lead to unintended if an argument includes spaces, sorry!");
        mavenOptsField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ProgramOptions.mavenOptions = mavenOptsField.getText();
            }
        });
        super.add(mavenOptsField);
    }

    private void setAndDisplayRuntime() {
        JFileChooser chooser = new JFileChooser(ProgramOptions.javaHome);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File dir = chooser.getSelectedFile();
        if (dir == null) return;

        ProgramOptions.javaHome = dir.getAbsolutePath();
        javaHomeField.setText(ProgramOptions.javaHome);

        try {
            ProcessBuilder pb = new ProcessBuilder(OSHelper.getPlatform().getPathFormatter()
                    .apply(String.format("%s%cbin%<cjava", ProgramOptions.javaHome, File.separatorChar)),
                    "-version")
                    .directory(BuildToolsGUI.CURRENT_DIRECTORY);
            pb.environment().put("JAVA_HOME", ProgramOptions.javaHome);
            Process proc = pb.start();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                JOptionPane.showMessageDialog(this, br.lines().collect(Collectors.joining("\n")));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, String.format("Could not retrieve Java Runtime information!%n%n%s",
                    ex.getMessage()), "Uh Oh!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
