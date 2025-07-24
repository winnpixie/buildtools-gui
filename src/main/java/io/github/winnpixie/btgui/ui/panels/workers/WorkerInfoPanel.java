package io.github.winnpixie.btgui.ui.panels.workers;

import io.github.winnpixie.btgui.utilities.SwingHelper;

import javax.swing.*;

public class WorkerInfoPanel extends JPanel {
    public WorkerInfoPanel() {
        super();

        super.add(SwingHelper.createLabel("Jobs you start will be displayed in this area."
                + "\nClose worker tabs by pressing 'Ctrl+W' once BuildTools has completed."));
    }
}
