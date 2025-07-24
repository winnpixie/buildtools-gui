package io.github.winnpixie.btgui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import io.github.winnpixie.btgui.configurations.BuildToolsConfiguration;
import io.github.winnpixie.btgui.configurations.JavaConfiguration;
import io.github.winnpixie.btgui.configurations.ProgramConfiguration;
import io.github.winnpixie.btgui.ui.MainWindow;
import io.github.winnpixie.logging.LogLevel;
import io.github.winnpixie.logging.PipedLogger;
import io.github.winnpixie.logging.pipes.JavaOutputPipe;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class BuildToolsGUI {
    public static final PipedLogger LOGGER = new PipedLogger(new JavaOutputPipe(Logger.getLogger(BuildToolsGUI.class.getName())));
    public static final AtomicInteger JOB_TRACKER = new AtomicInteger(0);

    public static final BuildToolsConfiguration.Builder BUILDTOOLS_CONFIGURATOR = new BuildToolsConfiguration.Builder();
    public static final JavaConfiguration.Builder JAVA_CONFIGURATOR = new JavaConfiguration.Builder();
    public static final ProgramConfiguration.Builder PROGRAM_CONFIGURATOR = new ProgramConfiguration.Builder();

    private static String version = "?.?.?";

    public static String getVersion() {
        return version;
    }

    public static void main(String[] args) {
        init();

        buildAndShowGui();
    }

    private static void init() {
        try (InputStream versionStream = BuildToolsGUI.class.getResourceAsStream("/version.properties")) {
            Properties properties = new Properties();
            properties.load(versionStream);
            version = properties.getProperty("BuildTools-GUI");
        } catch (IOException exception) {
            LOGGER.log(LogLevel.WARNING, exception, "Issue reading version properties");
        }
    }

    private static void buildAndShowGui() {
        SwingUtilities.invokeLater(() -> {
            FlatLaf.setPreferredFontFamily("Arial");
            FlatDarculaLaf.setup();

            new MainWindow(960, 640);
        });
    }
}