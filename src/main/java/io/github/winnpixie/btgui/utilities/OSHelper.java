package io.github.winnpixie.btgui.utilities;

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

    public enum Platform {
        WINDOWS(path -> String.format("\"%s\"", path)),
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
