package io.github.winnpixie.btgui.ui.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.utilities.SwingHelper;

import javax.swing.*;

public class AboutPanel extends JPanel {
    public AboutPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        super.add(SwingHelper.createLabel(String.format("BuildTools GUI (v%s)<hr />", BuildToolsGUI.getVersion()),
                5, 0, 930, 25));
        super.add(SwingHelper.createLabel("A third-party front-end for SpigotMC's BuildTools software.", 5, 25, 930, 25));
        super.add(SwingHelper.createHyperlinkedLabel("Developed by <a href=\"https://github.com/winnpixie/\">winnpixie</a>.",
                10, 50, 480, 25));
        super.add(SwingHelper.createHyperlinkedLabel("Source code available at " +
                        "<a href=\"https://github.com/winnpixie/buildtools-gui/\">github.com/winnpixie/buildtools-gui/</a>.",
                10, 75, 480, 25));

        super.add(SwingHelper.createLabel("Credits/Attributions<hr />", 5, 125, 930, 25));
        super.add(SwingHelper.createHyperlinkedLabel("<a href=\"https://spigotmc.org/\">SpigotMC</a> - Provides the " +
                        "<a href=\"https://hub.spigotmc.org/stash/projects/SPIGOT/repos/buildtools/browse\">BuildTools</a>" +
                        " software that this program utilizes.",
                10, 150, 480, 25));
        super.add(SwingHelper.createHyperlinkedLabel("<a href=\"https://www.formdev.com/\">FormDev</a> - " +
                        "<a href=\"https://www.formdev.com/flatlaf/\">FlatLaf</a>, an actually good looking Java Swing L&F.",
                10, 175, 480, 50));
    }
}
