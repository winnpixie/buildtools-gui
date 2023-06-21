package io.github.winnpixie.btgui.window.panels;

import io.github.winnpixie.btgui.utilities.SwingHelper;

import javax.swing.*;

public class AboutPanel extends JPanel {
    public AboutPanel() {
        super();

        super.setLayout(null);
        this.populateWithComponents();
    }

    private void populateWithComponents() {
        super.add(SwingHelper.createLabel("BuildTools GUI - An unofficial frontend for SpigotMC's BuildTools software.<hr />",
                5, 0, 930, 25));
        super.add(SwingHelper.createHyperlinkedLabel("Developed by Hannah " +
                        "(<a href=\"https://github.com/winnpixie/\">github.com/winnpixie/</a>)",
                10, 25, 480, 25));
        super.add(SwingHelper.createHyperlinkedLabel("Source code available at " +
                        "<a href=\"https://github.com/winnpixie/bt-gui/\">github.com/winnpixie/bt-gui/</a>",
                10, 50, 480, 25));

        super.add(SwingHelper.createLabel("Credits/Special Thanks<hr />", 5, 100, 930, 25));
        super.add(SwingHelper.createHyperlinkedLabel("<a href=\"https://github.com/md-5/\">md_5</a> - Develops the " +
                        "<a href=\"https://hub.spigotmc.org/stash/projects/SPIGOT/repos/buildtools/browse\">BuildTools</a>" +
                        " software that this program utilizes.",
                10, 125, 480, 25));
        super.add(SwingHelper.createHyperlinkedLabel("<a href=\"https://www.formdev.com/\">FormDev</a> - " +
                        "<a href=\"https://www.formdev.com/flatlaf/\">FlatLaf</a>, an actually good looking Java Swing L&F.",
                10, 150, 480, 50));
    }
}
