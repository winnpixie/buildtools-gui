package io.github.winnpixie.btgui.ui.panels.processing;

import io.github.winnpixie.btgui.utilities.SwingHelper;

import javax.swing.*;

public class ProcessInfoPanel extends JPanel {
    public ProcessInfoPanel() {
        super();

        super.add(SwingHelper.createLabel("Any BuildTools processes you start will show up in the tab bar above."
                + "<br />Close process tabs by pressing 'Ctrl+W' once BuildTools has completed."));
    }
}
