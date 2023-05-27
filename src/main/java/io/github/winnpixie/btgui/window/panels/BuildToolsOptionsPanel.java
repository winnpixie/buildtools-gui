package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.config.BuildToolsOptions;
import io.github.winnpixie.btgui.utilities.SwingHelper;
import io.github.winnpixie.btgui.window.components.WinnTextField;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class BuildToolsOptionsPanel extends JPanel {
    // Server
    private final JCheckBox skipGitPullOption = new JCheckBox("Don't Pull from Git", false);
    private final JCheckBox compileIfChangedOption = new JCheckBox("Only (Re-)Compile If Changed", false);

    // Server - Compilation Targets
    private final JCheckBox compileSpigotOption = new JCheckBox("Spigot", true);
    private final JCheckBox compileCraftBukkitOption = new JCheckBox("CraftBukkit", false);
    private final JCheckBox compileNothingOption = new JCheckBox("None", false);

    // Server - Version
    private final JTextField revision = new WinnTextField("latest", "Revision (ie. latest)");
    private final JTextField pullRequests = new WinnTextField("Pull Request(s) (ie. SPIGOT:120,SPIGOT:111)");
    private final JTextField outputDirectory = new WinnTextField(BuildToolsOptions.outputDirectory, "Output Directory");

    // Plugins
    private final JCheckBox generateSourcesOption = new JCheckBox("Generate Sources JAR(s)", false);
    private final JCheckBox generateJavadocsOption = new JCheckBox("Generate JavaDocs JAR(s)", false);
    private final JCheckBox remappedOption = new JCheckBox("MVN Install Remapped JAR(s)", false);

    // Advanced
    private final JCheckBox skipCertCheckOption = new JCheckBox("Skip HTTPS Certificate Check", false);
    private final JCheckBox skipJavaVersionCheckOption = new JCheckBox("Skip Java Version Check", false);
    private final JCheckBox developerModeOption = new JCheckBox("Run BuildTools in Dev Mode", false);

    private final JButton chooseOutputDir = new JButton("Browse...");

    public BuildToolsOptionsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        // Server
        super.add(SwingHelper.createLabel("Server Development", 0, 0, 200, 20));
        this.addServerOptions();

        // Plugins
        super.add(SwingHelper.createLabel("Plugin Development", 0, 180, 200, 20));
        this.addPluginOptions();

        // Advanced Options
        super.add(SwingHelper.createLabel("Advanced Options", 0, 280, 200, 20));
        this.addAdvancedOptions();
    }

    private void addServerOptions() {
        // Don't Pull from Git
        skipGitPullOption.setBounds(0, 20, 200, 20);
        SwingHelper.setTooltip(skipGitPullOption, "BuildTools CLI Arg: --dont-update");
        skipGitPullOption.addActionListener(e -> {
            BuildToolsOptions.skipGitPull = skipGitPullOption.isSelected();

            if (BuildToolsOptions.skipGitPull) {
                revision.setEditable(false);
                SwingHelper.setTooltip(revision, "Revisions can not be used when \"Don't Pull from Git\" is enabled.");
            } else if (!BuildToolsOptions.developerMode) {
                revision.setEditable(true);
                SwingHelper.setTooltip(revision, "BuildTools CLI Arg: --rev <REVISION>");
            }
        });
        super.add(skipGitPullOption);

        // Compile If Changed
        compileIfChangedOption.setBounds(0, 40, 200, 20);
        SwingHelper.setTooltip(compileIfChangedOption, "BuildTools CLI Arg: --compile-if-changed");
        super.add(compileIfChangedOption);

        // Compilation Targets
        super.add(SwingHelper.createLabel("Compilation Target(s)", 0, 80, 200, 20));

        // Spigot
        compileSpigotOption.setBounds(0, 100, 100, 20);
        SwingHelper.setTooltip(compileSpigotOption, "BuildTools CLI Arg: --compile SPIGOT");
        compileSpigotOption.addActionListener(e -> BuildToolsOptions.compileSpigot = compileSpigotOption.isSelected());
        super.add(compileSpigotOption);

        // CraftBukkit
        compileCraftBukkitOption.setBounds(100, 100, 100, 20);
        SwingHelper.setTooltip(compileCraftBukkitOption, "BuildTools CLI Arg: --compile CRAFTBUKKIT");
        compileCraftBukkitOption.addActionListener(e -> BuildToolsOptions.compileCraftBukkit = compileCraftBukkitOption.isSelected());
        super.add(compileCraftBukkitOption);

        // None
        compileNothingOption.setBounds(200, 100, 100, 20);
        SwingHelper.setTooltip(compileNothingOption, "BuildTools CLI Arg: --compile NONE (previously '--skip-compile')");
        compileNothingOption.addActionListener(e -> {
            compileSpigotOption.setEnabled(!compileNothingOption.isSelected());
            compileCraftBukkitOption.setEnabled(!compileNothingOption.isSelected());

            BuildToolsOptions.compileNothing = compileNothingOption.isSelected();
        });
        super.add(compileNothingOption);

        // Revision
        revision.setBounds(0, 120, 300, 20);
        SwingHelper.setTooltip(revision, "BuildTools CLI Arg: --rev <REVISION>");
        revision.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BuildToolsOptions.revision = revision.getText();
            }
        });
        super.add(revision);

        // Pull Requests
        pullRequests.setBounds(300, 120, 300, 20);
        SwingHelper.setTooltip(pullRequests, "BuildTools CLI Arg: --pull-request <REPO:ID>");
        pullRequests.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BuildToolsOptions.pullRequests = pullRequests.getText();
            }
        });
        super.add(pullRequests);

        // Output Directory
        outputDirectory.setBounds(0, 140, 500, 20);
        SwingHelper.setTooltip(outputDirectory, "BuildTools CLI Arg: --output-dir <DIR>");
        outputDirectory.setEditable(false);
        super.add(outputDirectory);

        // Set Output Directory
        chooseOutputDir.setBounds(500, 140, 100, 20);
        chooseOutputDir.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(BuildToolsGUI.CURRENT_DIRECTORY);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

            File dir = chooser.getSelectedFile();
            if (dir == null) return;

            BuildToolsOptions.outputDirectory = dir.getAbsolutePath();
            outputDirectory.setText(BuildToolsOptions.outputDirectory);
        });
        super.add(chooseOutputDir);
    }

    private void addPluginOptions() {
        // Gen Sources
        generateSourcesOption.setBounds(0, 200, 200, 20);
        SwingHelper.setTooltip(generateSourcesOption, "BuildTools CLI Arg: --generate-source");
        super.add(generateSourcesOption);

        // Gen Javadocs
        generateJavadocsOption.setBounds(0, 220, 200, 20);
        SwingHelper.setTooltip(generateJavadocsOption, "BuildTools CLI Arg: --generate-docs");
        super.add(generateJavadocsOption);

        // MVN Install Remapped
        remappedOption.setBounds(0, 240, 200, 20);
        SwingHelper.setTooltip(remappedOption, "BuildTools CLI Arg: --remapped");
        super.add(remappedOption);
    }

    private void addAdvancedOptions() {
        // Bypass HTTPS Cert
        skipCertCheckOption.setBounds(0, 300, 200, 20);
        SwingHelper.setTooltip(skipCertCheckOption, "BuildTools CLI Arg: --disable-certificate-check");
        skipCertCheckOption.addActionListener(e -> BuildToolsOptions.skipCertCheck = skipCertCheckOption.isSelected());
        super.add(skipCertCheckOption);

        // Bypass Java Version
        skipJavaVersionCheckOption.setBounds(0, 320, 200, 20);
        SwingHelper.setTooltip(skipJavaVersionCheckOption, "BuildTools CLI Arg: --disable-java-check");
        skipJavaVersionCheckOption.addActionListener(e -> BuildToolsOptions.skipJavaVersionCheck = skipJavaVersionCheckOption.isSelected());
        super.add(skipJavaVersionCheckOption);

        // Dev Mode
        developerModeOption.setBounds(0, 340, 200, 20);
        SwingHelper.setTooltip(developerModeOption, "BuildTools CLI Arg: --dev");
        developerModeOption.addActionListener(e -> {
            BuildToolsOptions.developerMode = developerModeOption.isSelected();

            if (BuildToolsOptions.developerMode) {
                revision.setEditable(false);
                SwingHelper.setTooltip(revision, "Revisions can not be used when Dev Mode is enabled.");
            } else if (!BuildToolsOptions.skipGitPull) {
                revision.setEditable(true);
                SwingHelper.setTooltip(revision, "BuildTools CLI Arg: --rev <REVISION>");
            }
        });
        super.add(developerModeOption);
    }
}
