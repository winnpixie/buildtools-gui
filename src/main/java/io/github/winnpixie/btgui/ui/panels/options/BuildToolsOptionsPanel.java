package io.github.winnpixie.btgui.ui.panels.options;

import io.github.winnpixie.btgui.config.BuildToolsOptions;
import io.github.winnpixie.btgui.ui.components.SOTextField;
import io.github.winnpixie.btgui.utilities.SwingHelper;
import io.github.winnpixie.btgui.utilities.SystemHelper;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class BuildToolsOptionsPanel extends JPanel {
    // Server
    private final JCheckBox skipGitPullOpt = new JCheckBox("Don't Pull from Git", false);
    private final JCheckBox compileIfChangedOpt = new JCheckBox("Only (Re-)Compile If Changed", false);

    // Server - Compilation Targets
    private final JCheckBox compileSpigotOpt = new JCheckBox("Spigot", true);
    private final JCheckBox compileCraftBukkitOpt = new JCheckBox("CraftBukkit", false);
    private final JCheckBox compileNothingOpt = new JCheckBox("None", false);

    // Server - Version
    private final JTextField revisionFld = new SOTextField("latest", "Revision (ie. latest)");
    private final JTextField pullRequestsFld = new SOTextField("Pull Request(s) (ie. SPIGOT:120,SPIGOT:111)");
    private final JTextField outputDirFld = new SOTextField(BuildToolsOptions.outputDirectory, "Output Directory");
    private final JButton selectOutputDirBtn = new JButton("Select ...");

    // Plugins
    private final JCheckBox genSourcesOpt = new JCheckBox("Generate Sources JAR(s)", false);
    private final JCheckBox genDocsOpt = new JCheckBox("Generate JavaDocs JAR(s)", false);
    private final JCheckBox remappedOpt = new JCheckBox("MVN Install Remapped JAR(s)", false);

    // Advanced
    private final JCheckBox skipHttpsCertCheckOpt = new JCheckBox("Skip HTTPS Certificate Check", false);
    private final JCheckBox skipJavaVersionCheckOpt = new JCheckBox("Skip Java Version Check", false);
    private final JCheckBox devModeOpt = new JCheckBox("Run BuildTools in Dev Mode", false);
    private final JCheckBox experimentalBuildOpt = new JCheckBox("Generate Experimental Build", false);

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
        skipGitPullOpt.setBounds(10, 25, 200, 25);
        SwingHelper.setTooltip(skipGitPullOpt, "BuildTools CLI Arg: --dont-update");
        skipGitPullOpt.addActionListener(e -> {
            BuildToolsOptions.skipGitPull = skipGitPullOpt.isSelected();

            if (BuildToolsOptions.skipGitPull) {
                revisionFld.setEditable(false);
                SwingHelper.setTooltip(revisionFld, "Revisions can not be used with Experimental Mode, Developer Mode," +
                        "\nor when \"Don't Pull from Git\" is enabled.");
            } else if (!BuildToolsOptions.developerMode && !BuildToolsOptions.experimentalMode) {
                revisionFld.setEditable(true);
                SwingHelper.setTooltip(revisionFld, "BuildTools CLI Arg: --rev &lt;revision&gt;");
            }
        });
        super.add(skipGitPullOpt);

        // Compile If Changed
        compileIfChangedOpt.setBounds(10, 50, 200, 25);
        SwingHelper.setTooltip(compileIfChangedOpt, "BuildTools CLI Arg: --compile-if-changed");
        super.add(compileIfChangedOpt);

        // Compilation Targets
        super.add(SwingHelper.createLabel("Compilation Target(s)", 10, 100, 200, 25));

        // Spigot
        compileSpigotOpt.setBounds(15, 125, 75, 25);
        SwingHelper.setTooltip(compileSpigotOpt, "BuildTools CLI Arg: --compile SPIGOT");
        compileSpigotOpt.addActionListener(e -> BuildToolsOptions.compileSpigot = compileSpigotOpt.isSelected());
        super.add(compileSpigotOpt);

        // CraftBukkit
        compileCraftBukkitOpt.setBounds(90, 125, 100, 25);
        SwingHelper.setTooltip(compileCraftBukkitOpt, "BuildTools CLI Arg: --compile CRAFTBUKKIT");
        compileCraftBukkitOpt.addActionListener(e -> BuildToolsOptions.compileCraftBukkit = compileCraftBukkitOpt.isSelected());
        super.add(compileCraftBukkitOpt);

        // None
        compileNothingOpt.setBounds(190, 125, 75, 25);
        SwingHelper.setTooltip(compileNothingOpt, "BuildTools CLI Arg: --compile NONE\n(previously '--skip-compile')");
        compileNothingOpt.addActionListener(e -> {
            compileSpigotOpt.setEnabled(!compileNothingOpt.isSelected());
            compileCraftBukkitOpt.setEnabled(!compileNothingOpt.isSelected());

            BuildToolsOptions.compileNothing = compileNothingOpt.isSelected();
        });
        super.add(compileNothingOpt);

        super.add(SwingHelper.createLabel("Revision/Version", 10, 150, 100, 25));
        // Revision
        revisionFld.setBounds(10, 175, 300, 25);
        SwingHelper.setTooltip(revisionFld, "BuildTools CLI Arg: --rev &lt;revision&gt;");
        revisionFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BuildToolsOptions.revision = revisionFld.getText();
            }
        });
        super.add(revisionFld);

        super.add(SwingHelper.createLabel("Pull Request(s)", 315, 150, 100, 25));
        // Pull Requests
        pullRequestsFld.setBounds(315, 175, 300, 25);
        SwingHelper.setTooltip(pullRequestsFld, "BuildTools CLI Arg: --pull-request &lt;repo:id&gt;");
        pullRequestsFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BuildToolsOptions.pullRequests = pullRequestsFld.getText();
            }
        });
        super.add(pullRequestsFld);

        super.add(SwingHelper.createLabel("Output Directory", 10, 200, 100, 25));
        // Output Directory
        outputDirFld.setBounds(10, 225, 500, 25);
        SwingHelper.setTooltip(outputDirFld, "BuildTools CLI Arg: --output-dir &lt;directory&gt;");
        super.add(outputDirFld);

        // Set Output Directory
        selectOutputDirBtn.setBounds(515, 225, 100, 25);
        selectOutputDirBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(SystemHelper.HOME_DIRECTORY);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

            File dir = chooser.getSelectedFile();
            if (dir == null) return;

            BuildToolsOptions.outputDirectory = dir.getAbsolutePath();
            outputDirFld.setText(BuildToolsOptions.outputDirectory);
        });
        super.add(selectOutputDirBtn);
    }

    private void addPluginOptions() {
        // Gen Sources
        genSourcesOpt.setBounds(10, 300, 200, 25);
        SwingHelper.setTooltip(genSourcesOpt, "BuildTools CLI Arg: --generate-source");
        genSourcesOpt.addActionListener(e -> BuildToolsOptions.generateSourcesJar = genSourcesOpt.isSelected());
        super.add(genSourcesOpt);

        // Gen Javadocs
        genDocsOpt.setBounds(10, 325, 200, 25);
        SwingHelper.setTooltip(genDocsOpt, "BuildTools CLI Arg: --generate-docs");
        genDocsOpt.addActionListener(e -> BuildToolsOptions.generateJavadocsJar = genDocsOpt.isSelected());
        super.add(genDocsOpt);

        // MVN Install Remapped
        remappedOpt.setBounds(10, 350, 200, 25);
        SwingHelper.setTooltip(remappedOpt, "BuildTools CLI Arg: --remapped");
        remappedOpt.addActionListener(e -> BuildToolsOptions.remapped = remappedOpt.isSelected());
        super.add(remappedOpt);
    }

    private void addAdvancedOptions() {
        // Bypass HTTPS Cert
        skipHttpsCertCheckOpt.setBounds(10, 425, 200, 25);
        SwingHelper.setTooltip(skipHttpsCertCheckOpt, "BuildTools CLI Arg: --disable-certificate-check");
        skipHttpsCertCheckOpt.addActionListener(e -> BuildToolsOptions.skipCertCheck = skipHttpsCertCheckOpt.isSelected());
        super.add(skipHttpsCertCheckOpt);

        // Bypass Java Version
        skipJavaVersionCheckOpt.setBounds(10, 450, 200, 25);
        SwingHelper.setTooltip(skipJavaVersionCheckOpt, "BuildTools CLI Arg: --disable-java-check");
        skipJavaVersionCheckOpt.addActionListener(e -> BuildToolsOptions.skipJavaVersionCheck = skipJavaVersionCheckOpt.isSelected());
        super.add(skipJavaVersionCheckOpt);

        // Dev Mode
        devModeOpt.setBounds(10, 475, 200, 25);
        SwingHelper.setTooltip(devModeOpt, "BuildTools CLI Arg: --dev");
        devModeOpt.addActionListener(e -> {
            BuildToolsOptions.developerMode = devModeOpt.isSelected();

            if (BuildToolsOptions.developerMode) {
                revisionFld.setEditable(false);
                SwingHelper.setTooltip(revisionFld, "Revisions can not be used with Experimental Mode, Developer Mode," +
                        "\nor when \"Don't Pull from Git\" is enabled.");
            } else if (!BuildToolsOptions.skipGitPull && !BuildToolsOptions.experimentalMode) {
                revisionFld.setEditable(true);
                SwingHelper.setTooltip(revisionFld, "BuildTools CLI Arg: --rev &lt;revision&gt;");
            }
        });
        super.add(devModeOpt);

        // Experimental Mode
        experimentalBuildOpt.setBounds(10, 500, 200, 25);
        SwingHelper.setTooltip(experimentalBuildOpt, "BuildTools CLI Arg: --experimental");
        experimentalBuildOpt.addActionListener(e -> {
            BuildToolsOptions.experimentalMode = experimentalBuildOpt.isSelected();

            if (BuildToolsOptions.experimentalMode) {
                revisionFld.setEditable(false);
                SwingHelper.setTooltip(revisionFld, "Revisions can not be used with Experimental Mode, Developer Mode," +
                        "\nor when \"Don't Pull from Git\" is enabled.");
            } else if (!BuildToolsOptions.skipGitPull && !BuildToolsOptions.developerMode) {
                revisionFld.setEditable(true);
                SwingHelper.setTooltip(revisionFld, "BuildTools CLI Arg: --rev &lt;revision&gt;");
            }
        });
        super.add(experimentalBuildOpt);
    }
}
