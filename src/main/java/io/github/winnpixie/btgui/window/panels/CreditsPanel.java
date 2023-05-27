package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.utilities.SwingHelper;

import javax.swing.*;

public class CreditsPanel extends JPanel {
    public CreditsPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        super.add(SwingHelper.createLabel("<a href=\"https://github.com/md-5/\">md_5</a> - Develops the " +
                        "<a href=\"https://hub.spigotmc.org/stash/projects/SPIGOT/repos/buildtools/browse\">BuildTools</a>" +
                        " software that this program utilizes.",
                0, 0, 600, 20));
    }
}
