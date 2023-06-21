package io.github.winnpixie.btgui;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.winnpixie.btgui.window.BTGUIWindow;

import java.io.File;
import java.util.logging.Logger;

public class BuildToolsGUI {
    public static final String VERSION = "0.0.3";
    public static final Logger LOGGER = Logger.getLogger(BuildToolsGUI.class.getName());
    public static final File CURRENT_DIRECTORY = new File(System.getProperty("user.dir", "."));

    public static boolean RUNNING = false;

    public static void main(String[] args) {
        LOGGER.info("BEGIN bt-gui init");

        FlatDarkLaf.setup();
        new BTGUIWindow(960, 640);

        LOGGER.info("END bt-gui init");
    }
}