package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.utilities.BTGUIOptions;

import javax.swing.*;

public class BTGUIOptionsPanel extends JPanel {
    private final JCheckBox downloadBuildToolsOption = new JCheckBox("Download BuildTools JAR", true);
    private final JCheckBox randomRunDirOption = new JCheckBox("Create Fresh Runs", true);
    private final JCheckBox deleteOnFinishOption = new JCheckBox("Delete files on Finish", false);

    private final JTextField javaHome = new JTextField(BTGUIOptions.javaHome);

    private final JButton chooseJavaHomeBtn = new JButton("Set (JDK) JAVA_HOME");

    public BTGUIOptionsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        downloadBuildToolsOption.setBounds(0, 0, 200, 20);
        super.add(downloadBuildToolsOption);

        randomRunDirOption.setBounds(0, 20, 200, 20);
        super.add(randomRunDirOption);

        deleteOnFinishOption.setBounds(0, 40, 200, 20);
        super.add(deleteOnFinishOption);

        javaHome.setBounds(0, 80, 300, 20);
        super.add(javaHome);

        chooseJavaHomeBtn.setBounds(300, 80, 200, 20);
        super.add(chooseJavaHomeBtn);
    }
}
