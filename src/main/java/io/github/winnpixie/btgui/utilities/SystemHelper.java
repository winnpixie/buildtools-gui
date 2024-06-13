package io.github.winnpixie.btgui.utilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Function;

public class SystemHelper {
    public static final File CURRENT_DIRECTORY = new File(System.getProperty("user.dir"));
    public static final File HOME_DIRECTORY = new File(System.getProperty("user.home"));
    public static final Platform PLATFORM;

    static {
        PLATFORM = System.getProperty("os.name").contains("Windows")
                ? Platform.WINDOWS : Platform.UNIX;
    }

    public static void openFolder(File file) {
        if (!file.isDirectory()) return; // TODO: Is this... ever an issue?

        try {
            Desktop.getDesktop().open(file);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        openLink(file.toURI());
    }

    public static void openLink(URL url) {
        try {
            openLink(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void openLink(URI uri) {
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Platform {
        WINDOWS(path -> path.indexOf(' ') < 0 ? path : String.format("\"%s\"", path)),
        UNIX(path -> path.replace(" ", "\\ "));

        private final Function<String, String> pathFormatter;

        Platform(Function<String, String> pathFormatter) {
            this.pathFormatter = pathFormatter;
        }

        public Function<String, String> getPathFormatter() {
            return pathFormatter;
        }
    }
}
