package io.github.winnpixie.btgui.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class IOHelper {
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;

        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }

        return outputStream.toByteArray();
    }

    public static byte[] getBytes(String url) throws IOException {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.setRequestProperty("User-Agent", "Java/BT-GUI ( https://github.com/winnpixie/bt-gui/ )");

            try (InputStream inputStream = connection.getInputStream()) {
                return getBytes(inputStream);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void delete(File file) throws IOException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    delete(child);
                }
            }
        }

        Files.deleteIfExists(file.toPath());
    }
}
