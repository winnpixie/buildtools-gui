package io.github.winnpixie.btgui.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

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
            conn.setRequestProperty("User-Agent", "Java/BT-GUI ( https://github.com/winnpixie/bt-gui/ )");

            try (InputStream inputStream = conn.getInputStream()) {
                return getBytes(inputStream);
            }
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    public static void delete(File file) throws IOException {
        if (!file.isDirectory()) {
            Files.deleteIfExists(file.toPath());
            return;
        }

        File[] children = file.listFiles();
        if (children == null) return;

        for (File child : children) {
            delete(child);
        }
    }
}
