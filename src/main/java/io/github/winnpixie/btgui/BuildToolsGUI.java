package io.github.winnpixie.btgui;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.winnpixie.btgui.ui.windows.main.MainWindow;
import io.github.winnpixie.logging.CustomLogger;
import io.github.winnpixie.logging.outputs.JavaOutputDevice;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class BuildToolsGUI {
    public static final String VERSION = "0.0.4";
    public static final CustomLogger LOGGER = new CustomLogger(new JavaOutputDevice(Logger.getLogger(BuildToolsGUI.class.getName())));
    public static final File CURRENT_DIRECTORY = new File(System.getProperty("user.dir", "."));

    private static final AtomicInteger ACTIVE_BUILDS = new AtomicInteger();

    public static int getActiveBuilds() {
        return ACTIVE_BUILDS.get();
    }

    public static void addBuild() {
        ACTIVE_BUILDS.incrementAndGet();
    }

    public static void removeBuild() {
        ACTIVE_BUILDS.decrementAndGet();
    }

    public static void main(String[] args) {
        LOGGER.info("BEGIN bt-gui init");

        FlatDarkLaf.setup();
        new MainWindow(960, 640);

        LOGGER.info("END bt-gui init");
    }
}