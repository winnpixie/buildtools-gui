package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.config.ProgramOptions;
import io.github.winnpixie.btgui.window.JTextFieldWithPlaceholder;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class BuildToolsGUIOptionsPanel extends JPanel {
    private final JCheckBox downloadBuildToolsOption = new JCheckBox("Download BuildTools JAR", true);
    private final JCheckBox isolateRuns = new JCheckBox("Isolate Runs", true);
    private final JCheckBox deleteRunOnFinish = new JCheckBox("Delete Run on Finish", true);

    private final JTextField javaHome = new JTextFieldWithPlaceholder(ProgramOptions.javaHome, "JAVA_HOME");
    private final JTextField heapSize = new JTextFieldWithPlaceholder(ProgramOptions.heapSize, "Heap Size (ie. 1024M)");

    private final JButton chooseJavaHome = new JButton("Set JAVA_HOME");

    public BuildToolsGUIOptionsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        downloadBuildToolsOption.setBounds(0, 0, 200, 20);
        downloadBuildToolsOption.addActionListener(e -> ProgramOptions.downloadBuildTools = downloadBuildToolsOption.isSelected());
        super.add(downloadBuildToolsOption);

        isolateRuns.setBounds(0, 20, 200, 20);
        isolateRuns.addActionListener(e -> ProgramOptions.isolateRuns = isolateRuns.isSelected());
        super.add(isolateRuns);

        deleteRunOnFinish.setBounds(0, 40, 200, 20);
        deleteRunOnFinish.addActionListener(e -> ProgramOptions.deleteRunOnFinish = deleteRunOnFinish.isSelected());
        super.add(deleteRunOnFinish);

        javaHome.setBounds(0, 80, 300, 20);
        javaHome.setEditable(false);
        super.add(javaHome);

        chooseJavaHome.setBounds(300, 80, 200, 20);
        chooseJavaHome.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(ProgramOptions.javaHome);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

            File dir = chooser.getSelectedFile();
            if (dir == null) return;

            ProgramOptions.javaHome = dir.getAbsolutePath();
            javaHome.setText(ProgramOptions.javaHome);

            try {
                ProcessBuilder pb = new ProcessBuilder(String.format("\"%s%sbin%sjava.exe\"",
                        ProgramOptions.javaHome,
                        File.separatorChar, File.separatorChar),
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
        super.add(chooseJavaHome);

        heapSize.setBounds(0, 100, 200, 20);
        heapSize.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ProgramOptions.heapSize = heapSize.getText();
            }
        });
        super.add(heapSize);
    }
}
