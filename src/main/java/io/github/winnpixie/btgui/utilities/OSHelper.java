package io.github.winnpixie.btgui.utilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class OSHelper {
    private static Platform platform;

    public static Platform getPlatform() {
        if (platform == null) {
            platform = Platform.UNIX;

            if (System.getProperty("os.name").contains("Windows")) {
                platform = Platform.WINDOWS;
            }
        }

        return platform;
    }

    public static void showDirectory(File file) {
        if (!file.isDirectory()) return; // TODO: Is this... ever an issue?

        try {
            Desktop.getDesktop().open(file);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Desktop.getDesktop().browse(file.toURI());
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
