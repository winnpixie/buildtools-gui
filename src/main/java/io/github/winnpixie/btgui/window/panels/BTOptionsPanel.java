package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.utilities.BTOptions;
import io.github.winnpixie.btgui.window.JTextFieldWithPlaceholder;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BTOptionsPanel extends JPanel {
    private final JCheckBox skipCertCheckOption = new JCheckBox("Skip HTTPS Certificate Check", false);
    private final JCheckBox skipJavaVersionCheckOption = new JCheckBox("Skip Java Version Check", false);
    private final JCheckBox skipGitPullOption = new JCheckBox("Skip 'git pull'", false);
    private final JCheckBox generateSourcesOption = new JCheckBox("Generate Sources JAR(s)", false);
    private final JCheckBox generateJavadocsOption = new JCheckBox("Generate JavaDocs JAR(s)", false);
    private final JCheckBox remappedOption = new JCheckBox("Build & MVN Install Remapped JAR(s)", false);
    private final JCheckBox compileIfChangedOption = new JCheckBox("Only (Re-)Compile If Changed", false);
    private final JCheckBox developerModeOption = new JCheckBox("Run BuildTools in Dev Mode", false);
    private final JCheckBox compileSpigotOption = new JCheckBox("Compile Spigot", true);
    private final JCheckBox compileCraftBukkitOption = new JCheckBox("Compile CraftBukkit", false);
    private final JCheckBox compileNothingOption = new JCheckBox("Don't Compile", false);

    private final JTextFieldWithPlaceholder revision = new JTextFieldWithPlaceholder("latest", "Revision (ie. latest)");
    private final JTextFieldWithPlaceholder pullRequests = new JTextFieldWithPlaceholder("Pull Request(s) (ie. SPIGOT:120,SPIGOT:111)");
    private final JTextFieldWithPlaceholder outputDirectory = new JTextFieldWithPlaceholder("Output Directory");

    private final JButton chooseOutputDirBtn = new JButton("Set Output Directory");

    public BTOptionsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        skipCertCheckOption.setBounds(0, 0, 200, 20);
        skipCertCheckOption.setToolTipText("BuildTools CLI Arg: --disable-certificate-check");
        skipCertCheckOption.addActionListener(e -> BTOptions.skipCertCheck = skipCertCheckOption.isSelected());
        super.add(skipCertCheckOption);

        skipJavaVersionCheckOption.setBounds(0, 20, 200, 20);
        skipJavaVersionCheckOption.setToolTipText("BuildTools CLI Arg: --disable-java-check");
        skipJavaVersionCheckOption.addActionListener(e -> BTOptions.skipJavaVersionCheck = skipJavaVersionCheckOption.isSelected());
        super.add(skipJavaVersionCheckOption);

        skipGitPullOption.setBounds(0, 40, 200, 20);
        skipGitPullOption.setToolTipText("BuildTools CLI Arg: --dont-update");
        skipGitPullOption.addActionListener(e -> {
            BTOptions.skipGitPull = skipGitPullOption.isSelected();

            if (BTOptions.skipGitPull) {
                revision.setEditable(false);
                revision.setToolTipText("Revisions can not be used when \"Skip 'git pull'\" is enabled.");
            } else if (!BTOptions.developerMode) {
                revision.setEditable(true);
                revision.setToolTipText("BuildTools CLI Arg: --rev <REVISION>");
            }
        });
        super.add(skipGitPullOption);

        generateSourcesOption.setBounds(0, 60, 200, 20);
        generateSourcesOption.setToolTipText("BuildTools CLI Arg: --generate-source");
        super.add(generateSourcesOption);

        generateJavadocsOption.setBounds(0, 80, 200, 20);
        generateJavadocsOption.setToolTipText("BuildTools CLI Arg: --generate-docs");
        super.add(generateJavadocsOption);

        remappedOption.setBounds(0, 100, 200, 20);
        remappedOption.setToolTipText("BuildTools CLI Arg: --remapped");
        super.add(remappedOption);

        compileIfChangedOption.setBounds(0, 120, 200, 20);
        compileIfChangedOption.setToolTipText("BuildTools CLI Arg: --compile-if-changed");
        super.add(compileIfChangedOption);

        developerModeOption.setBounds(0, 140, 200, 20);
        developerModeOption.setToolTipText("BuildTools CLI Arg: --dev");
        developerModeOption.addActionListener(e -> {
            BTOptions.developerMode = developerModeOption.isSelected();

            if (BTOptions.developerMode) {
                revision.setEditable(false);
                revision.setToolTipText("Revisions can not be used when Dev Mode is enabled.");
            } else if (!BTOptions.skipGitPull) {
                revision.setEditable(true);
                revision.setToolTipText("BuildTools CLI Arg: --rev <REVISION>");
            }
        });
        super.add(developerModeOption);

        compileSpigotOption.setBounds(0, 180, 200, 20);
        compileSpigotOption.setToolTipText("BuildTools CLI Arg: --compile SPIGOT");
        compileSpigotOption.addActionListener(e -> BTOptions.compileSpigot = compileSpigotOption.isSelected());
        super.add(compileSpigotOption);

        compileCraftBukkitOption.setBounds(200, 180, 200, 20);
        compileCraftBukkitOption.setToolTipText("BuildTools CLI Arg: --compile CRAFTBUKKIT");
        compileCraftBukkitOption.addActionListener(e -> BTOptions.compileCraftBukkit = compileCraftBukkitOption.isSelected());
        super.add(compileCraftBukkitOption);

        compileNothingOption.setBounds(400, 180, 200, 20);
        compileNothingOption.setToolTipText("BuildTools CLI Arg: --compile NONE (previously '--skip-compile')");
        compileNothingOption.addActionListener(e -> {
            compileSpigotOption.setEnabled(!compileNothingOption.isSelected());
            compileCraftBukkitOption.setEnabled(!compileNothingOption.isSelected());

            BTOptions.compileNothing = compileNothingOption.isSelected();
        });
        super.add(compileNothingOption);

        revision.setBounds(0, 200, 300, 20);
        revision.setToolTipText("BuildTools CLI Arg: --rev <REVISION>");
        revision.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BTOptions.revision = revision.getText();
            }
        });
        super.add(revision);

        pullRequests.setBounds(300, 200, 300, 20);
        pullRequests.setToolTipText("BuildTools CLI Arg: --pull-request <REPO:ID>");
        pullRequests.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BTOptions.pullRequests = pullRequests.getText();
            }
        });
        super.add(pullRequests);

        outputDirectory.setBounds(0, 220, 300, 20);
        outputDirectory.setToolTipText("BuildTools CLI Arg: --output-dir <DIR>");
        outputDirectory.setEditable(false);
        super.add(outputDirectory);

        chooseOutputDirBtn.setBounds(300, 220, 200, 20);
        chooseOutputDirBtn.addActionListener(e -> {
            // TODO: File Chooser
        });
        super.add(chooseOutputDirBtn);
    }
}
