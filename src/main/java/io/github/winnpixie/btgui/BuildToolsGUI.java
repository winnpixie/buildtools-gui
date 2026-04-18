package io.github.winnpixie.btgui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import io.github.winnpixie.btgui.configurations.BuildToolsConfiguration;
import io.github.winnpixie.btgui.configurations.JavaConfiguration;
import io.github.winnpixie.btgui.configurations.ProgramConfiguration;
import io.github.winnpixie.btgui.ui.MainWindow;
import io.github.winnpixie.btgui.utilities.IOHelper;
import io.github.winnpixie.logging.LogLevel;
import io.github.winnpixie.logging.PipedLogger;
import io.github.winnpixie.logging.pipes.JavaOutputPipe;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class BuildToolsGUI {
    public static final PipedLogger LOGGER = new PipedLogger(new JavaOutputPipe(Logger.getLogger(BuildToolsGUI.class.getName())));
    public static final AtomicInteger JOB_TRACKER = new AtomicInteger(0);
    public static final ExecutorService WORKER_SERVICE = Executors.newCachedThreadPool();

    public static final BuildToolsConfiguration.Builder BUILDTOOLS_CONFIGURATOR = new BuildToolsConfiguration.Builder();
    public static final JavaConfiguration.Builder JAVA_CONFIGURATOR = new JavaConfiguration.Builder();
    public static final ProgramConfiguration.Builder PROGRAM_CONFIGURATOR = new ProgramConfiguration.Builder();

    public static final String VERSION;

    static {
        String version = "UNKNOWN";
        try (InputStream versionStream = IOHelper.getResourceStream("/version.properties")) {
            Properties properties = new Properties();
            properties.load(versionStream);

            version = properties.getProperty("BuildTools-GUI");
        } catch (IOException ioe) {
            LOGGER.log(LogLevel.WARNING, ioe, "reading version.properties");
        } finally {
            VERSION = version;
        }
    }

    public static void main(String[] args) {
        init();

        buildAndShowGui();
    }

    private static void init() {
        // Request antialiasing for text
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Request hardware acceleration for rendering
        requestHardwareAcceleration();
    }

    private static void toggleDirectX(boolean state) {
        System.setProperty("sun.java2d.noddraw", Boolean.toString(!state));
        System.setProperty("sun.java2d.ddoffscreen", Boolean.toString(state));
        System.setProperty("sun.java2d.d3d", Boolean.toString(state));
    }

    private static void toggleOpenGL(boolean state) {
        System.setProperty("sun.java2d.opengl", Boolean.toString(state));
    }

    private static void requestHardwareAcceleration() {
        // Default to OpenGL as a fix for issues with DirectX on Windows
        String engine = System.getProperty("btgui.hw_accel", "ogl");

        switch (engine.toLowerCase()) {
            case "dx": // DirectX (DirectDraw + Direct3D)
                toggleDirectX(true);
                toggleOpenGL(false);
                break;
            case "ogl": // OpenGL
                toggleDirectX(false);
                toggleOpenGL(true);
                break;
            case "sw": // Software Engine (OFF)
                toggleDirectX(false);
                toggleOpenGL(false);
                break;
            default: // Unknown
                // Either use user-defined arguments, or let the JVM decide
                break;
        }
    }

    private static void buildAndShowGui() {
        SwingUtilities.invokeLater(() -> {
            FlatLaf.setPreferredFontFamily("Verdana");
            FlatDarculaLaf.setup();

            new MainWindow(960, 640);
        });
    }
}