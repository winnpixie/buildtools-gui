package io.github.winnpixie.btgui.utilities;

import io.github.winnpixie.btgui.BuildToolsGUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JARHelper {
    public static Map<String, Class<?>> loadClasses(File file) throws IOException {
        Map<String, Class<?>> classMap = new HashMap<>();
        ClassLoader clsLoader = BuildToolsGUI.class.getClassLoader();
        URLClassLoader urlLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, clsLoader);
        Thread.currentThread().setContextClassLoader(urlLoader);

        try (JarFile jar = new JarFile(file)) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.getName().endsWith(".class")) continue;
                String clsName = formatEntryName(entry.getName());

                try {
                    classMap.put(clsName, urlLoader.loadClass(clsName));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return classMap;
    }

    public static byte[] getResource(File file, String resourceName) throws IOException {
        try (JarFile jar = new JarFile(file)) {
            JarEntry entry = jar.getJarEntry(resourceName);
            if (entry == null) return null;

            return IOHelper.getBytes(jar.getInputStream(entry));
        }
    }

    private static String formatEntryName(String name) {
        name = name.replaceAll("[\\\\/]", ".");
        return name.substring(0, name.length() - 6);
    }
}
