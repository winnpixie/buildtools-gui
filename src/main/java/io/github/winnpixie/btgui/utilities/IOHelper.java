package io.github.winnpixie.btgui.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOHelper {
    public static byte[] getBytes(InputStream streamIn) throws IOException {
        ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;

        while ((read = streamIn.read(buffer)) != -1) {
            streamOut.write(buffer, 0, read);
        }

        return streamOut.toByteArray();
    }

    public static byte[] getBytes(String url) throws IOException {
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) (new URL(url)).openConnection();
            conn.setRequestProperty("User-Agent", "winnpixie/buildtools-gui ( https://github.com/winnpixie/buildtools-gui/ )");

            try (InputStream inputStream = conn.getInputStream()) {
                return getBytes(inputStream);
            }
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    public static void deleteRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> directory = Files.newDirectoryStream(path)) {
                for (Path child : directory) deleteRecursively(child);
            }
        }

        Files.deleteIfExists(path);
    }
}