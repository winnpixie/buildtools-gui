package io.github.winnpixie.btgui.ui.panels.workers;

import io.github.winnpixie.btgui.utilities.SwingHelper;

import javax.swing.*;

public class WorkerInfoPanel extends JPanel {
    public WorkerInfoPanel() {
        super();

        super.add(SwingHelper.createLabel("Jobs you start will be displayed in this area."
                + "\nClose tabs by pressing 'Ctrl+W' after a worker has completed."
                + "\nYou may press 'Ctrl+D' to forcefully terminate a worker."));
    }
}
