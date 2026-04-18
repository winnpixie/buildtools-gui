package io.github.winnpixie.btgui.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class IOHelper {
    private IOHelper() {
    }

    public static InputStream getResourceStream(String path) throws IOException {
        InputStream stream = IOHelper.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IOException(String.format("'%s' could not be located", path));
        }

        return stream;
    }

    public static <T extends OutputStream> T transfer(InputStream src, T dst) throws IOException {
        byte[] buffer = new byte[1024]; // 1K buffer
        int read;

        while ((read = src.read(buffer)) != -1) {
            dst.write(buffer, 0, read);
        }

        return dst;
    }

    public static byte[] toByteArray(InputStream src) throws IOException {
        return transfer(src, new ByteArrayOutputStream()).toByteArray();
    }

    public static byte[] getBinary(String url) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);

            connection.setRequestProperty("User-Agent", "buildtools-gui");

            try (InputStream input = connection.getInputStream()) {
                return toByteArray(input);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void deleteRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> directory = Files.newDirectoryStream(path)) {
                for (Path child : directory) {
                    deleteRecursively(child);
                }
            }
        }

        Files.deleteIfExists(path);
    }
}