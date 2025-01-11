package io.github.winnpixie.btgui;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.winnpixie.btgui.ui.MainWindow;

import javax.swing.*;

public class BuildToolsGUI {
    public static final String VERSION = "0.0.6";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setPreferredFontFamily("Arial");
            FlatDarkLaf.setup();
            new MainWindow(960, 640);
        });
    }
}