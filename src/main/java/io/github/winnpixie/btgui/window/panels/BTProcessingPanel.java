package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.utilities.BTGUIOptions;
import io.github.winnpixie.btgui.utilities.BTOptions;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class BTProcessingPanel extends JPanel {
    private final JTextField commandToRun = new JTextField();
    private final JButton prepareBtn = new JButton("Prepare");
    private final JButton runBtn = new JButton("Run");
    private final JTextArea outputLog = new JTextArea();

    public BTProcessingPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        commandToRun.setBounds(0, 0, 400, 20);
        commandToRun.setToolTipText("The command that will run BuildTools.jar");
        commandToRun.setEditable(false);
        super.add(commandToRun);

        prepareBtn.setBounds(400, 0, 100, 20);
        prepareBtn.addActionListener(e -> commandToRun.setText(BTOptions.buildCommand(BTGUIOptions.javaHome, "BuildTools.jar")));
        super.add(prepareBtn);

        runBtn.setBounds(500, 0, 100, 20);
        runBtn.addActionListener(e -> {
            File buildToolsFile = new File(BuildToolsGUI.CURRENT_DIRECTORY, "BuildTools.jar");
            if (BTGUIOptions.downloadBuildTools) {
                try {
                    byte[] btData = downloadBuildTools();

                    Files.write(buildToolsFile.toPath(), btData,
                            StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            File runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, "run");
            if (BTGUIOptions.randomizeDirectory) {
                runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, String.format("run-%d", System.currentTimeMillis()));
            }

            if (!runDir.exists()) {
                runDir.mkdir();
            }

            File realBuildToolsFile = new File(runDir, "BuildTools.jar");
            try {
                Files.copy(buildToolsFile.toPath(), realBuildToolsFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        super.add(runBtn);

        outputLog.setBounds(0, 20, 620, 420);
        outputLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputLog);
        scrollPane.setBounds(outputLog.getBounds());
        super.add(scrollPane);
    }

    private byte[] downloadBuildTools() throws Exception {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) (new URL("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar")).openConnection();
            conn.setRequestProperty("User-Agent", "BuildTools GUI (github.com/winnpixie/bt-gui/)");
            conn.setInstanceFollowRedirects(true);

            try (InputStream is = conn.getInputStream()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int read;

                while ((read = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }

                return baos.toByteArray();
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
