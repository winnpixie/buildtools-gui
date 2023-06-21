package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.config.ProgramOptions;
import io.github.winnpixie.btgui.utilities.OSHelper;
import io.github.winnpixie.btgui.utilities.SwingHelper;
import io.github.winnpixie.btgui.window.components.SOTextField;

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
    private final JCheckBox deleteOnFinishOption = new JCheckBox("Delete Run on Finish", true);

    private final JTextField javaHomeField = new SOTextField(ProgramOptions.javaHome, "JAVA_HOME");
    private final JTextField heapSizesField = new SOTextField(ProgramOptions.heapSize, "Heap Size (ie. 1024M)");

    private final JButton setJavaHomeBtn = new JButton("Browse...");

    public ProgramOptionsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        downloadBuildToolsOption.setBounds(5, 0, 200, 25);
        SwingHelper.setTooltip(downloadBuildToolsOption, "Download's BuildTools.jar from SpigotMC Jenkins.");
        downloadBuildToolsOption.addActionListener(e -> ProgramOptions.downloadBuildTools = downloadBuildToolsOption.isSelected());
        super.add(downloadBuildToolsOption);

        isolateRunsOption.setBounds(5, 25, 200, 25);
        SwingHelper.setTooltip(isolateRunsOption, "Creates a new directory with a random name when BuildTools is ran," +
                "\nuseful for building different Minecraft versions.");
        isolateRunsOption.addActionListener(e -> ProgramOptions.isolateRuns = isolateRunsOption.isSelected());
        super.add(isolateRunsOption);

        deleteOnFinishOption.setBounds(5, 50, 200, 25);
        SwingHelper.setTooltip(deleteOnFinishOption, "Deletes the BuildTools run directory when it is completed.");
        deleteOnFinishOption.addActionListener(e -> ProgramOptions.deleteRunOnFinish = deleteOnFinishOption.isSelected());
        super.add(deleteOnFinishOption);

        super.add(SwingHelper.createLabel("JAVA_HOME Path", 5, 100, 200, 25));
        javaHomeField.setBounds(5, 125, 500, 25);
        SwingHelper.setTooltip(javaHomeField, "The directory of the JDK to run BuildTools with.");
        javaHomeField.setEditable(false);
        super.add(javaHomeField);

        setJavaHomeBtn.setBounds(510, 125, 100, 25);
        setJavaHomeBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(ProgramOptions.javaHome);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

            File dir = chooser.getSelectedFile();
            if (dir == null) return;

            ProgramOptions.javaHome = dir.getAbsolutePath();
            javaHomeField.setText(ProgramOptions.javaHome);

            try {
                ProcessBuilder pb = new ProcessBuilder(OSHelper.getPlatform().getPathFormatter()
                        .apply(String.format("%s%cbin%cjava", ProgramOptions.javaHome, File.separatorChar, File.separatorChar)),
                        "-version")
                        .directory(BuildToolsGUI.CURRENT_DIRECTORY);
                pb.environment().put("JAVA_HOME", ProgramOptions.javaHome);
                Process proc = pb.start();

                try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                    JOptionPane.showMessageDialog(this, br.lines().collect(Collectors.joining("\n")));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        super.add(setJavaHomeBtn);

        super.add(SwingHelper.createLabel("JVM Heap Sizes (-Xms & -Xmx)", 5, 175, 200, 25));
        heapSizesField.setBounds(5, 200, 200, 25);
        SwingHelper.setTooltip(heapSizesField, "Java CLI Arg(s): -Xms / -Xmx");
        heapSizesField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ProgramOptions.heapSize = heapSizesField.getText();
            }
        });
        super.add(heapSizesField);
    }
}
