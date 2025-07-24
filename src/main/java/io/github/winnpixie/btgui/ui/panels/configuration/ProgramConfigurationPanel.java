package io.github.winnpixie.btgui.ui.panels.configuration;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.utilities.SwingHelper;

import javax.swing.*;

public class ProgramConfigurationPanel extends JPanel {
    private final JCheckBox downloadBuildToolsOpt = new JCheckBox("Download BuildTools JAR", true);
    private final JCheckBox isolateProcessesOpt = new JCheckBox("Isolate Runs", true);
    private final JCheckBox showOutputOnFinishOpt = new JCheckBox("Open Output Directory on Finish", true);
    private final JCheckBox deleteWorkOnFinishOpt = new JCheckBox("Delete Run Directory on Finish", false);

    public ProgramConfigurationPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        // Download BuildTools
        downloadBuildToolsOpt.setBounds(5, 0, 480, 25);
        SwingHelper.setTooltip(downloadBuildToolsOpt, "Download's BuildTools.jar from SpigotMC Jenkins.");
        downloadBuildToolsOpt.addActionListener(e -> BuildToolsGUI.PROGRAM_CONFIGURATOR.downloadBuildTools(downloadBuildToolsOpt.isSelected()));
        super.add(downloadBuildToolsOpt);

        // Isolate Runs
        isolateProcessesOpt.setBounds(5, 25, 480, 25);
        SwingHelper.setTooltip(isolateProcessesOpt, "Creates a new directory with a random name when BuildTools is ran," +
                "\nuseful for building different Minecraft versions.");
        isolateProcessesOpt.addActionListener(e -> BuildToolsGUI.PROGRAM_CONFIGURATOR.isolateProcesses(isolateProcessesOpt.isSelected()));
        super.add(isolateProcessesOpt);

        // Open Output Directory on Finish
        showOutputOnFinishOpt.setBounds(5, 50, 480, 25);
        SwingHelper.setTooltip(showOutputOnFinishOpt, "Opens the BuildTools output directory when it is completed.");
        showOutputOnFinishOpt.addActionListener(e -> BuildToolsGUI.PROGRAM_CONFIGURATOR.showOutputOnFinish(showOutputOnFinishOpt.isSelected()));
        super.add(showOutputOnFinishOpt);

        // Delete Run Directory on Finish
        deleteWorkOnFinishOpt.setBounds(5, 75, 480, 25);
        SwingHelper.setTooltip(deleteWorkOnFinishOpt, "Deletes the BuildTools run directory when it is completed.");
        deleteWorkOnFinishOpt.addActionListener(e -> BuildToolsGUI.PROGRAM_CONFIGURATOR.deleteWorkOnFinish(deleteWorkOnFinishOpt.isSelected()));
        super.add(deleteWorkOnFinishOpt);
    }
}
