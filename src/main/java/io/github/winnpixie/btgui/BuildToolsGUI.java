package io.github.winnpixie.btgui;

import io.github.winnpixie.btgui.window.BTWindow;

import java.io.File;
import java.util.logging.Logger;

public class BuildToolsGUI {
    public static final String VERSION = "0.0.1-dev";
    public static final Logger LOGGER = Logger.getLogger(BuildToolsGUI.class.getName());
    public static final File CURRENT_DIRECTORY = new File(".");

    public static void main(String[] args) {
        LOGGER.info("BEGIN bt-gui init");

        new BTWindow();

        LOGGER.info("END bt-gui init");
    }
}