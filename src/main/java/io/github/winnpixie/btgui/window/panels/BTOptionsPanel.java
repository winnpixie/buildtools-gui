package io.github.winnpixie.btgui.window.panels;

import javax.swing.*;

public class BTOptionsPanel extends JPanel {
    private final JCheckBox skipCertCheckOption = new JCheckBox("Skip HTTPS Certificate Check", false);
    private final JCheckBox skipJavaVersionCheckOption = new JCheckBox("Skip Java Version Check", false);
    private final JCheckBox skipGitPullOption = new JCheckBox("Skip 'git pull'", false);
    private final JCheckBox dontCompileOption = new JCheckBox("Skip Compilation", false);
    private final JCheckBox generateSourcesOption = new JCheckBox("Generate Sources JAR(s)", false);
    private final JCheckBox generateJavadocsOption = new JCheckBox("Generate JavaDocs JAR(s)", false);
    private final JCheckBox developerModeOption = new JCheckBox("Developer Mode (BuildTools)", false);
    private final JCheckBox remappedOption = new JCheckBox("Build & MVN Install Remapped JAR(s)", false);
    private final JCheckBox compileIfChangedOption = new JCheckBox("Only (Re-)Compile If Changed", false);

    private final JCheckBox compileSpigot = new JCheckBox("Compile Spigot", true);
    private final JCheckBox compileCraftBukkit = new JCheckBox("Compile CraftBukkit", false);

    private final JTextField pullRequests = new JTextField();

    private final JTextField outputDirectory = new JTextField();
    private final JButton chooseOutputDirBtn = new JButton("Choose Folder...");

    public BTOptionsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    public void populateWithComponents() {
        skipCertCheckOption.setBounds(0, 0, 200, 20);
        super.add(skipCertCheckOption);

        skipJavaVersionCheckOption.setBounds(0, 20, 200, 20);
        super.add(skipJavaVersionCheckOption);

        skipGitPullOption.setBounds(0, 40, 200, 20);
        super.add(skipGitPullOption);

        dontCompileOption.setBounds(0, 60, 200, 20);
        super.add(dontCompileOption);

        generateSourcesOption.setBounds(0, 80, 200, 20);
        super.add(generateSourcesOption);

        generateJavadocsOption.setBounds(0, 100, 200, 20);
        super.add(generateJavadocsOption);

        developerModeOption.setBounds(0, 120, 200, 20);
        super.add(developerModeOption);

        remappedOption.setBounds(0, 140, 200, 20);
        super.add(remappedOption);

        compileIfChangedOption.setBounds(0, 160, 200, 20);
        super.add(compileIfChangedOption);

        compileSpigot.setBounds(0, 200, 200, 20);
        super.add(compileSpigot);

        compileCraftBukkit.setBounds(200, 200, 200, 20);
        super.add(compileCraftBukkit);
    }
}
