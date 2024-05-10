package io.github.winnpixie.btgui.ui.windows.main.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.config.BuildToolsOptions;
import io.github.winnpixie.btgui.ui.components.SOTextField;
import io.github.winnpixie.btgui.utilities.SwingHelper;

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
    private final JTextField revisionField = new SOTextField("latest", "Revision (ie. latest)");
    private final JTextField pullRequestsField = new SOTextField("Pull Request(s) (ie. SPIGOT:120,SPIGOT:111)");
    private final JTextField outputDirField = new SOTextField(BuildToolsOptions.outputDirectory, "Output Directory");
    private final JButton selectOutputDirButton = new JButton("Select ...");

    // Plugins
    private final JCheckBox generateSourcesOption = new JCheckBox("Generate Sources JAR(s)", false);
    private final JCheckBox generateJavadocsOption = new JCheckBox("Generate JavaDocs JAR(s)", false);
    private final JCheckBox remappedOption = new JCheckBox("MVN Install Remapped JAR(s)", false);

    // Advanced
    private final JCheckBox skipCertCheckOption = new JCheckBox("Skip HTTPS Certificate Check", false);
    private final JCheckBox skipJavaVersionCheckOption = new JCheckBox("Skip Java Version Check", false);
    private final JCheckBox developerModeOption = new JCheckBox("Run BuildTools in Dev Mode", false);
    private final JCheckBox experimentalModeOption = new JCheckBox("Generate Experimental Build", false);

    public BuildToolsOptionsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        super.add(SwingHelper.createLabel("Server Development<hr />", 5, 0, 930, 25));
        this.addServerOptions();

        super.add(SwingHelper.createLabel("Plugin Development<hr />", 5, 275, 930, 25));
        this.addPluginOptions();

        super.add(SwingHelper.createLabel("Advanced Options<hr />", 5, 400, 930, 25));
        this.addAdvancedOptions();
    }

    private void addServerOptions() {
        // Don't Pull from Git
        skipGitPullOption.setBounds(10, 25, 200, 25);
        SwingHelper.setTooltip(skipGitPullOption, "BuildTools CLI Arg: --dont-update");
        skipGitPullOption.addActionListener(e -> {
            BuildToolsOptions.skipGitPull = skipGitPullOption.isSelected();

            if (BuildToolsOptions.skipGitPull) {
                revisionField.setEditable(false);
                SwingHelper.setTooltip(revisionField, "Revisions can not be used with Experimental Mode, Developer Mode," +
                        "\nor when \"Don't Pull from Git\" is enabled.");
            } else if (!BuildToolsOptions.developerMode && !BuildToolsOptions.experimentalMode) {
                revisionField.setEditable(true);
                SwingHelper.setTooltip(revisionField, "BuildTools CLI Arg: --rev &lt;revision&gt;");
            }
        });
        super.add(skipGitPullOption);

        // Compile If Changed
        compileIfChangedOption.setBounds(10, 50, 200, 25);
        SwingHelper.setTooltip(compileIfChangedOption, "BuildTools CLI Arg: --compile-if-changed");
        super.add(compileIfChangedOption);

        // Compilation Targets
        super.add(SwingHelper.createLabel("Compilation Target(s)", 10, 100, 200, 25));

        // Spigot
        compileSpigotOption.setBounds(15, 125, 75, 25);
        SwingHelper.setTooltip(compileSpigotOption, "BuildTools CLI Arg: --compile SPIGOT");
        compileSpigotOption.addActionListener(e -> BuildToolsOptions.compileSpigot = compileSpigotOption.isSelected());
        super.add(compileSpigotOption);

        // CraftBukkit
        compileCraftBukkitOption.setBounds(90, 125, 100, 25);
        SwingHelper.setTooltip(compileCraftBukkitOption, "BuildTools CLI Arg: --compile CRAFTBUKKIT");
        compileCraftBukkitOption.addActionListener(e -> BuildToolsOptions.compileCraftBukkit = compileCraftBukkitOption.isSelected());
        super.add(compileCraftBukkitOption);

        // None
        compileNothingOption.setBounds(190, 125, 75, 25);
        SwingHelper.setTooltip(compileNothingOption, "BuildTools CLI Arg: --compile NONE\n(previously '--skip-compile')");
        compileNothingOption.addActionListener(e -> {
            compileSpigotOption.setEnabled(!compileNothingOption.isSelected());
            compileCraftBukkitOption.setEnabled(!compileNothingOption.isSelected());

            BuildToolsOptions.compileNothing = compileNothingOption.isSelected();
        });
        super.add(compileNothingOption);

        super.add(SwingHelper.createLabel("Revision/Version", 10, 150, 100, 25));
        // Revision
        revisionField.setBounds(10, 175, 300, 25);
        SwingHelper.setTooltip(revisionField, "BuildTools CLI Arg: --rev &lt;revision&gt;");
        revisionField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BuildToolsOptions.revision = revisionField.getText();
            }
        });
        super.add(revisionField);

        super.add(SwingHelper.createLabel("Pull Request(s)", 315, 150, 100, 25));
        // Pull Requests
        pullRequestsField.setBounds(315, 175, 300, 25);
        SwingHelper.setTooltip(pullRequestsField, "BuildTools CLI Arg: --pull-request &lt;repo:id&gt;");
        pullRequestsField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BuildToolsOptions.pullRequests = pullRequestsField.getText();
            }
        });
        super.add(pullRequestsField);

        super.add(SwingHelper.createLabel("Output Directory", 10, 200, 100, 25));
        // Output Directory
        outputDirField.setBounds(10, 225, 500, 25);
        SwingHelper.setTooltip(outputDirField, "BuildTools CLI Arg: --output-dir &lt;directory&gt;");
        super.add(outputDirField);

        // Set Output Directory
        selectOutputDirButton.setBounds(515, 225, 100, 25);
        selectOutputDirButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(BuildToolsGUI.CURRENT_DIRECTORY);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

            File dir = chooser.getSelectedFile();
            if (dir == null) return;

            BuildToolsOptions.outputDirectory = dir.getAbsolutePath();
            outputDirField.setText(BuildToolsOptions.outputDirectory);
        });
        super.add(selectOutputDirButton);
    }

    private void addPluginOptions() {
        // Gen Sources
        generateSourcesOption.setBounds(10, 300, 200, 25);
        SwingHelper.setTooltip(generateSourcesOption, "BuildTools CLI Arg: --generate-source");
        generateSourcesOption.addActionListener(e -> BuildToolsOptions.generateSourcesJar = generateSourcesOption.isSelected());
        super.add(generateSourcesOption);

        // Gen Javadocs
        generateJavadocsOption.setBounds(10, 325, 200, 25);
        SwingHelper.setTooltip(generateJavadocsOption, "BuildTools CLI Arg: --generate-docs");
        generateJavadocsOption.addActionListener(e -> BuildToolsOptions.generateJavadocsJar = generateJavadocsOption.isSelected());
        super.add(generateJavadocsOption);

        // MVN Install Remapped
        remappedOption.setBounds(10, 350, 200, 25);
        SwingHelper.setTooltip(remappedOption, "BuildTools CLI Arg: --remapped");
        remappedOption.addActionListener(e -> BuildToolsOptions.remapped = remappedOption.isSelected());
        super.add(remappedOption);
    }

    private void addAdvancedOptions() {
        // Bypass HTTPS Cert
        skipCertCheckOption.setBounds(10, 425, 200, 25);
        SwingHelper.setTooltip(skipCertCheckOption, "BuildTools CLI Arg: --disable-certificate-check");
        skipCertCheckOption.addActionListener(e -> BuildToolsOptions.skipCertCheck = skipCertCheckOption.isSelected());
        super.add(skipCertCheckOption);

        // Bypass Java Version
        skipJavaVersionCheckOption.setBounds(10, 450, 200, 25);
        SwingHelper.setTooltip(skipJavaVersionCheckOption, "BuildTools CLI Arg: --disable-java-check");
        skipJavaVersionCheckOption.addActionListener(e -> BuildToolsOptions.skipJavaVersionCheck = skipJavaVersionCheckOption.isSelected());
        super.add(skipJavaVersionCheckOption);

        // Dev Mode
        developerModeOption.setBounds(10, 475, 200, 25);
        SwingHelper.setTooltip(developerModeOption, "BuildTools CLI Arg: --dev");
        developerModeOption.addActionListener(e -> {
            BuildToolsOptions.developerMode = developerModeOption.isSelected();

            if (BuildToolsOptions.developerMode) {
                revisionField.setEditable(false);
                SwingHelper.setTooltip(revisionField, "Revisions can not be used with Experimental Mode, Developer Mode," +
                        "\nor when \"Don't Pull from Git\" is enabled.");
            } else if (!BuildToolsOptions.skipGitPull && !BuildToolsOptions.experimentalMode) {
                revisionField.setEditable(true);
                SwingHelper.setTooltip(revisionField, "BuildTools CLI Arg: --rev &lt;revision&gt;");
            }
        });
        super.add(developerModeOption);

        // Experimental Mode
        experimentalModeOption.setBounds(10, 500, 200, 25);
        SwingHelper.setTooltip(experimentalModeOption, "BuildTools CLI Arg: --experimental");
        experimentalModeOption.addActionListener(e -> {
            BuildToolsOptions.experimentalMode = experimentalModeOption.isSelected();

            if (BuildToolsOptions.experimentalMode) {
                revisionField.setEditable(false);
                SwingHelper.setTooltip(revisionField, "Revisions can not be used with Experimental Mode, Developer Mode," +
                        "\nor when \"Don't Pull from Git\" is enabled.");
            } else if (!BuildToolsOptions.skipGitPull && !BuildToolsOptions.developerMode) {
                revisionField.setEditable(true);
                SwingHelper.setTooltip(revisionField, "BuildTools CLI Arg: --rev &lt;revision&gt;");
            }
        });
        super.add(experimentalModeOption);
    }
}
