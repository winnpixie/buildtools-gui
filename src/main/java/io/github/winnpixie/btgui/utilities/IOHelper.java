package io.github.winnpixie.btgui.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IOHelper {
    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;

        while ((read = is.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }

        return baos.toByteArray();
    }

    public static byte[] getBytes(String url) throws IOException {
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) (new URL(url)).openConnection();

            try (InputStream is = conn.getInputStream()) {
                return getBytes(is);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    delete(child);
                }
            }
        }

        file.delete();
    }
}
