package io.github.winnpixie.btgui.ui.panels.configuration;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.ui.components.SOTextField;
import io.github.winnpixie.btgui.utilities.SwingHelper;
import io.github.winnpixie.btgui.utilities.SystemHelper;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class BuildToolsConfigurationPanel extends JPanel {
    // Server
    private final JCheckBox dontUpdateOpt = new JCheckBox("Don't Pull from Git", false);
    private final JCheckBox compileIfChangedOpt = new JCheckBox("Only Compile If Changed", false);

    // Server - Compilation Targets
    private final JCheckBox compileSpigotOpt = new JCheckBox("Spigot", true);
    private final JCheckBox compileCraftBukkitOpt = new JCheckBox("CraftBukkit", false);
    private final JCheckBox compileNothingOpt = new JCheckBox("None", false);

    // Server - Version
    private final JTextField revisionFld = new SOTextField("latest", "Revision (ie. latest)");
    private final JTextField pullRequestsFld = new SOTextField("Pull Request(s) (ie. SPIGOT:120,SPIGOT:111)");
    private final JTextField outputDirFld = new SOTextField("", "Output Directory, click \"Browse\" to set...");
    private final JButton selectOutputDirBtn = new JButton("Browse");
    private final JTextField finalNameFld = new SOTextField("", "Final Name (ie. server.jar)");

    // Plugins
    private final JCheckBox genSourcesOpt = new JCheckBox("Generate Sources", false);
    private final JCheckBox genDocsOpt = new JCheckBox("Generate JavaDocs", false);
    private final JCheckBox remappedOpt = new JCheckBox("Install Remapped to Maven Local", false);

    // Advanced
    private final JCheckBox skipHttpsCertCheckOpt = new JCheckBox("Skip HTTPS Certificate Check", false);
    private final JCheckBox skipJavaVersionCheckOpt = new JCheckBox("Skip Java Version Check", false);
    private final JCheckBox devModeOpt = new JCheckBox("Developer Mode", false);
    private final JCheckBox experimentalOpt = new JCheckBox("Generate Experimental Build", false);

    public BuildToolsConfigurationPanel() {
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
        dontUpdateOpt.setBounds(10, 25, 480, 25);
        SwingHelper.setTooltip(dontUpdateOpt, "BuildTools CLI: --dont-update");
        dontUpdateOpt.addActionListener(e -> {
            BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.dontUpdate(dontUpdateOpt.isSelected());

            toggleRevisionField();
        });
        super.add(dontUpdateOpt);

        // Compile If Changed
        compileIfChangedOpt.setBounds(10, 50, 480, 25);
        SwingHelper.setTooltip(compileIfChangedOpt, "BuildTools CLI: --compile-if-changed");
        compileIfChangedOpt.addActionListener(e -> BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.compileIfChanged(compileIfChangedOpt.isSelected()));
        super.add(compileIfChangedOpt);

        // Compilation Targets
        super.add(SwingHelper.createLabel("Compilation Target(s)", 10, 100, 480, 25));

        // Spigot
        compileSpigotOpt.setBounds(15, 125, 75, 25);
        SwingHelper.setTooltip(compileSpigotOpt, "BuildTools CLI: --compile SPIGOT");
        compileSpigotOpt.addActionListener(e -> updateCompileTargets());
        super.add(compileSpigotOpt);

        // CraftBukkit
        compileCraftBukkitOpt.setBounds(90, 125, 100, 25);
        SwingHelper.setTooltip(compileCraftBukkitOpt, "BuildTools CLI: --compile CRAFTBUKKIT");
        compileCraftBukkitOpt.addActionListener(e -> updateCompileTargets());
        super.add(compileCraftBukkitOpt);

        // None
        compileNothingOpt.setBounds(190, 125, 75, 25);
        SwingHelper.setTooltip(compileNothingOpt, "BuildTools CLI: --compile NONE\n(previously '--skip-compile')");
        compileNothingOpt.addActionListener(e -> {
            compileSpigotOpt.setEnabled(!compileNothingOpt.isSelected());
            compileCraftBukkitOpt.setEnabled(!compileNothingOpt.isSelected());

            updateCompileTargets();
        });
        super.add(compileNothingOpt);

        super.add(SwingHelper.createLabel("Revision/Version", 10, 150, 480, 25));
        // Revision
        revisionFld.setBounds(10, 175, 300, 25);
        SwingHelper.setTooltip(revisionFld, "BuildTools CLI: --rev REVISION");
        revisionFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.revision(revisionFld.getText());
            }
        });
        super.add(revisionFld);

        super.add(SwingHelper.createLabel("Pull Request(s)", 315, 150, 480, 25));
        // Pull Requests
        pullRequestsFld.setBounds(315, 175, 300, 25);
        SwingHelper.setTooltip(pullRequestsFld, "BuildTools CLI: --pull-request REPO:ID");
        pullRequestsFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String pullRequests = pullRequestsFld.getText();
                if (!pullRequests.isEmpty()) {
                    BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.pullRequests(pullRequests.split(","));
                } else {
                    BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.pullRequests(new String[0]);
                }
            }
        });
        super.add(pullRequestsFld);

        super.add(SwingHelper.createLabel("Output Directory", 210, 200, 480, 25));
        // Output Directory
        outputDirFld.setBounds(210, 225, 300, 25);
        SwingHelper.setTooltip(outputDirFld, "BuildTools CLI: --output-dir PATH");
        outputDirFld.setEditable(false);
        super.add(outputDirFld);

        // Set Output Directory
        selectOutputDirBtn.setBounds(515, 225, 100, 25);
        selectOutputDirBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(SystemHelper.HOME_DIRECTORY);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

            File dir = chooser.getSelectedFile();
            if (dir == null) return;

            String selectedPath = dir.getAbsolutePath();
            BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.outputDirectory(selectedPath);
            outputDirFld.setText(selectedPath);
        });
        super.add(selectOutputDirBtn);

        super.add(SwingHelper.createLabel("Final Name", 10, 200, 480, 25));
        // Final Name
        finalNameFld.setBounds(10, 225, 195, 25);
        SwingHelper.setTooltip(finalNameFld, "BuildTools CLI: --final-name NAME\nLeave blank for default names.");
        finalNameFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.finalName(finalNameFld.getText());
            }
        });
        super.add(finalNameFld);
    }

    private void addPluginOptions() {
        // Gen Sources
        genSourcesOpt.setBounds(10, 300, 480, 25);
        SwingHelper.setTooltip(genSourcesOpt, "BuildTools CLI: --generate-source");
        genSourcesOpt.addActionListener(e -> BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.generateSources(genSourcesOpt.isSelected()));
        super.add(genSourcesOpt);

        // Gen Javadocs
        genDocsOpt.setBounds(10, 325, 480, 25);
        SwingHelper.setTooltip(genDocsOpt, "BuildTools CLI: --generate-docs");
        genDocsOpt.addActionListener(e -> BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.generateJavadocs(genDocsOpt.isSelected()));
        super.add(genDocsOpt);

        // MVN Install Remapped
        remappedOpt.setBounds(10, 350, 480, 25);
        SwingHelper.setTooltip(remappedOpt, "BuildTools CLI: --remapped");
        remappedOpt.addActionListener(e -> BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.remapped(remappedOpt.isSelected()));
        super.add(remappedOpt);
    }

    private void addAdvancedOptions() {
        // Bypass HTTPS Cert
        skipHttpsCertCheckOpt.setBounds(10, 425, 480, 25);
        SwingHelper.setTooltip(skipHttpsCertCheckOpt, "BuildTools CLI: --disable-certificate-check");
        skipHttpsCertCheckOpt.addActionListener(e -> BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.disableCertificateCheck(skipHttpsCertCheckOpt.isSelected()));
        super.add(skipHttpsCertCheckOpt);

        // Bypass Java Version
        skipJavaVersionCheckOpt.setBounds(10, 450, 480, 25);
        SwingHelper.setTooltip(skipJavaVersionCheckOpt, "BuildTools CLI: --disable-java-check");
        skipJavaVersionCheckOpt.addActionListener(e -> BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.disableJavaCheck(skipJavaVersionCheckOpt.isSelected()));
        super.add(skipJavaVersionCheckOpt);

        // Dev Mode
        devModeOpt.setBounds(10, 475, 480, 25);
        SwingHelper.setTooltip(devModeOpt, "BuildTools CLI: --dev");
        devModeOpt.addActionListener(e -> {
            BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.developerMode(devModeOpt.isSelected());

            toggleRevisionField();
        });
        super.add(devModeOpt);

        // Experimental Mode
        experimentalOpt.setBounds(10, 500, 480, 25);
        SwingHelper.setTooltip(experimentalOpt, "BuildTools CLI: --experimental");
        experimentalOpt.addActionListener(e -> {
            BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.experimental(experimentalOpt.isSelected());

            toggleRevisionField();
        });
        super.add(experimentalOpt);
    }

    private void updateCompileTargets() {
        if (compileNothingOpt.isSelected()) {
            BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.addCompileTarget("NONE");
            BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.removeCompileTarget("SPIGOT");
            BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.removeCompileTarget("CRAFTBUKKIT");
        } else {
            BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.removeCompileTarget("NONE");

            if (compileSpigotOpt.isSelected()) {
                BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.addCompileTarget("SPIGOT");
            } else {
                BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.removeCompileTarget("SPIGOT");
            }

            if (compileCraftBukkitOpt.isSelected()) {
                BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.addCompileTarget("CRAFTBUKKIT");
            } else {
                BuildToolsGUI.BUILDTOOLS_CONFIGURATOR.removeCompileTarget("CRAFTBUKKIT");
            }
        }
    }

    private void toggleRevisionField() {
        if (dontUpdateOpt.isSelected() || devModeOpt.isSelected() || experimentalOpt.isSelected()) {
            revisionFld.setEditable(false);
            SwingHelper.setTooltip(revisionFld, "Revisions can not be used with Experimental Mode, Developer Mode," +
                    "\nor when \"Don't Pull from Git\" is enabled.");
        } else {
            revisionFld.setEditable(true);
            SwingHelper.setTooltip(revisionFld, "BuildTools CLI: --rev REVISION");
        }
    }
}
