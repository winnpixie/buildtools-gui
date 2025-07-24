package io.github.winnpixie.btgui.ui.panels.configuration;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.ui.components.SOTextField;
import io.github.winnpixie.btgui.utilities.SwingHelper;
import io.github.winnpixie.btgui.utilities.SystemHelper;
import io.github.winnpixie.logging.LogLevel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class JavaConfigurationPanel extends JPanel {
    private final JTextField javaHomeFld = new SOTextField(SystemHelper.getDefaultJavaHome(), "JAVA_HOME, click \"Browse\" to set...");
    private final JButton selectJavaHomeBtn = new JButton("Browse");

    private final JTextField jvmArgsFld = new SOTextField("-Xmx1024M -Xms1024M", "JVM Arguments");
    private final JTextField mavenOptsFld = new SOTextField(SystemHelper.getDefaultMavenOptions(), "MAVEN_OPTS");

    public JavaConfigurationPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        super.add(SwingHelper.createLabel("JAVA_HOME Path", 5, 0, 480, 25));
        // JAVA_HOME
        javaHomeFld.setBounds(5, 25, 505, 25);
        SwingHelper.setTooltip(javaHomeFld, "The directory of the JDK to run BuildTools with.");
        javaHomeFld.setEditable(false);
        super.add(javaHomeFld);

        // Set JAVA_HOME
        selectJavaHomeBtn.setBounds(515, 25, 100, 25);
        selectJavaHomeBtn.addActionListener(e -> testAndSetRuntime());
        super.add(selectJavaHomeBtn);

        super.add(SwingHelper.createLabel("JVM Arguments", 5, 75, 480, 25));
        // JVM Arguments
        jvmArgsFld.setBounds(5, 100, 610, 25);
        SwingHelper.setTooltip(jvmArgsFld, "Examples: -Xms512M, -Xmx1024M, etc...\n" +
                "Do NOT comma-separate arguments, only spaces.\n" +
                "This WILL lead to unintended if an argument includes spaces, sorry!");
        jvmArgsFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BuildToolsGUI.JAVA_CONFIGURATOR.javaArguments(jvmArgsFld.getText());
            }
        });
        super.add(jvmArgsFld);

        super.add(SwingHelper.createLabel("MAVEN_OPTS Arguments", 5, 150, 480, 25));
        // MAVEN_OPTS
        mavenOptsFld.setBounds(5, 175, 610, 25);
        SwingHelper.setTooltip(mavenOptsFld, "Similar to JVM Arguments, but for Maven.\n" +
                "Examples: -Xms512M, -Xmx1024M, etc...\n" +
                "Do NOT comma-separate arguments, only spaces.\n" +
                "This WILL lead to unintended if an argument includes spaces, sorry!");
        mavenOptsFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BuildToolsGUI.JAVA_CONFIGURATOR.mavenArguments(mavenOptsFld.getText());
            }
        });
        super.add(mavenOptsFld);
    }

    private void testAndSetRuntime() {
        JFileChooser chooser = new JFileChooser(SystemHelper.getDefaultJavaHome());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File selection = chooser.getSelectedFile();
        if (selection == null) return;

        String selectedPath = selection.getAbsolutePath();

        try {
            ProcessBuilder procBuilder = new ProcessBuilder(SystemHelper.getJavaExecutable(selectedPath),
                    "-version");
            procBuilder.environment().put("JAVA_HOME", selectedPath);
            Process proc = procBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                JOptionPane.showMessageDialog(this, reader.lines().collect(Collectors.joining("\n")));
            }

            BuildToolsGUI.JAVA_CONFIGURATOR.javaHome(selectedPath);
            javaHomeFld.setText(selectedPath);
        } catch (IOException exception) {
            BuildToolsGUI.LOGGER.log(LogLevel.SEVERE, exception, "Error retrieving Java Runtime information");

            JOptionPane.showMessageDialog(this, String.format("Could not set JAVA_HOME!%n%n%s: %s",
                    exception.getClass().getSimpleName(), exception.getMessage()), "Uh Oh!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
