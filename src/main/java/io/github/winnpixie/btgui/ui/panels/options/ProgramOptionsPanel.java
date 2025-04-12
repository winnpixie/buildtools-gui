package io.github.winnpixie.btgui.ui.panels.options;

import io.github.winnpixie.btgui.options.ProgramOptions;
import io.github.winnpixie.btgui.ui.components.SOTextField;
import io.github.winnpixie.btgui.utilities.SwingHelper;
import io.github.winnpixie.btgui.utilities.SystemHelper;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ProgramOptionsPanel extends JPanel {
    private final JCheckBox downloadBuildToolsOpt = new JCheckBox("Download BuildTools JAR", true);
    private final JCheckBox isolateRunsOpt = new JCheckBox("Isolate Runs", true);
    private final JCheckBox openOutputDirOnFinishOpt = new JCheckBox("Open Output Directory on Finish", true);
    private final JCheckBox deleteRunDirOnFinishOpt = new JCheckBox("Delete Run Directory on Finish", false);

    private final JTextField javaHomeFld = new SOTextField(ProgramOptions.TEMPLATE.javaHome, "JAVA_HOME");
    private final JButton selectJavaHomeBtn = new JButton("Browse");

    private final JTextField jvmArgsFld = new SOTextField(ProgramOptions.TEMPLATE.jvmArguments, "JVM Arguments");

    private final JTextField mavenOptsFld = new SOTextField(ProgramOptions.TEMPLATE.mavenOptions, "MAVEN_OPTS");


    public ProgramOptionsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        // Download BuildTools
        downloadBuildToolsOpt.setBounds(5, 0, 480, 25);
        SwingHelper.setTooltip(downloadBuildToolsOpt, "Download's BuildTools.jar from SpigotMC Jenkins.");
        downloadBuildToolsOpt.addActionListener(e -> ProgramOptions.TEMPLATE.downloadBuildTools = downloadBuildToolsOpt.isSelected());
        super.add(downloadBuildToolsOpt);

        // Isolate Runs
        isolateRunsOpt.setBounds(5, 25, 480, 25);
        SwingHelper.setTooltip(isolateRunsOpt, "Creates a new directory with a random name when BuildTools is ran," +
                "\nuseful for building different Minecraft versions.");
        isolateRunsOpt.addActionListener(e -> ProgramOptions.TEMPLATE.isolateRuns = isolateRunsOpt.isSelected());
        super.add(isolateRunsOpt);

        // Open Output Directory on Finish
        openOutputDirOnFinishOpt.setBounds(5, 50, 480, 25);
        SwingHelper.setTooltip(openOutputDirOnFinishOpt, "Opens the BuildTools output directory when it is completed.");
        openOutputDirOnFinishOpt.addActionListener(e -> ProgramOptions.TEMPLATE.openOutputAfterFinish = openOutputDirOnFinishOpt.isSelected());
        super.add(openOutputDirOnFinishOpt);

        // Delete Run Directory on Finish
        deleteRunDirOnFinishOpt.setBounds(5, 75, 480, 25);
        SwingHelper.setTooltip(deleteRunDirOnFinishOpt, "Deletes the BuildTools run directory when it is completed.");
        deleteRunDirOnFinishOpt.addActionListener(e -> ProgramOptions.TEMPLATE.deleteWorkDirOnFinish = deleteRunDirOnFinishOpt.isSelected());
        super.add(deleteRunDirOnFinishOpt);

        super.add(SwingHelper.createLabel("JAVA_HOME Path", 5, 125, 480, 25));
        // JAVA_HOME
        javaHomeFld.setBounds(5, 150, 505, 25);
        SwingHelper.setTooltip(javaHomeFld, "The directory of the JDK to run BuildTools with.");
        super.add(javaHomeFld);

        // Set JAVA_HOME
        selectJavaHomeBtn.setBounds(515, 150, 100, 25);
        selectJavaHomeBtn.addActionListener(e -> setAndDisplayRuntime());
        super.add(selectJavaHomeBtn);

        super.add(SwingHelper.createLabel("JVM Arguments", 5, 200, 480, 25));
        // JVM Arguments
        jvmArgsFld.setBounds(5, 225, 610, 25);
        SwingHelper.setTooltip(jvmArgsFld, "Examples: -Xms512M, -Xmx1024M, etc...\n" +
                "Do NOT comma-separate arguments, only spaces.\n" +
                "This WILL lead to unintended if an argument includes spaces, sorry!");
        jvmArgsFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ProgramOptions.TEMPLATE.jvmArguments = jvmArgsFld.getText();
            }
        });
        super.add(jvmArgsFld);

        super.add(SwingHelper.createLabel("MAVEN_OPTS Arguments", 5, 275, 480, 25));
        // MAVEN_OPTS
        mavenOptsFld.setBounds(5, 300, 610, 25);
        SwingHelper.setTooltip(mavenOptsFld, "Similar to JVM Arguments, but for Maven.\n" +
                "Examples: -Xms512M, -Xmx1024M, etc...\n" +
                "Do NOT comma-separate arguments, only spaces.\n" +
                "This WILL lead to unintended if an argument includes spaces, sorry!");
        mavenOptsFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ProgramOptions.TEMPLATE.mavenOptions = mavenOptsFld.getText();
            }
        });
        super.add(mavenOptsFld);
    }

    private void setAndDisplayRuntime() {
        JFileChooser chooser = new JFileChooser(ProgramOptions.TEMPLATE.javaHome);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File dir = chooser.getSelectedFile();
        if (dir == null) return;

        ProgramOptions.TEMPLATE.javaHome = dir.getAbsolutePath();
        javaHomeFld.setText(ProgramOptions.TEMPLATE.javaHome);

        try {
            ProcessBuilder pb = new ProcessBuilder(SystemHelper.PLATFORM.getPathFormatter()
                    .apply(String.format("%s%cbin%<cjava", ProgramOptions.TEMPLATE.javaHome, File.separatorChar)),
                    "-version");
            pb.environment().put("JAVA_HOME", ProgramOptions.TEMPLATE.javaHome);
            Process proc = pb.start();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                JOptionPane.showMessageDialog(this, br.lines().collect(Collectors.joining("\n")));
            }
        } catch (IOException ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(this, String.format("Could not retrieve Java Runtime information!%n%n%s",
                    ex.getMessage()), "Uh Oh!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
