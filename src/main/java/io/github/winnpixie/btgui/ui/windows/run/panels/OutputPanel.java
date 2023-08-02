package io.github.winnpixie.btgui.ui.windows.run.panels;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.ui.windows.run.RunWindow;
import io.github.winnpixie.logging.outputs.TextAreaOutputDevice;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class OutputPanel extends JPanel {
    private final RunWindow window;
    private final JTextField javaCommand = new JTextField();
    private final JTextField buildToolsArguments = new JTextField();
    private final JTextArea outputArea = new JTextArea();

    public OutputPanel(RunWindow window) {
        super();

        this.window = window;

        super.setLayout(null);
        populateWithComponents();

        runExecutor();
    }

    private void populateWithComponents() {
        javaCommand.setBounds(5, 0, 455, 30);
        javaCommand.setEditable(false);
        javaCommand.setText(String.join(" ", window.getExecutor().getJavaCommand()));
        super.add(javaCommand);

        buildToolsArguments.setBounds(5, 30, 455, 30);
        buildToolsArguments.setEditable(false);
        buildToolsArguments.setText(String.join(" ", window.getExecutor().getBuildToolsArguments()));
        super.add(buildToolsArguments);

        outputArea.setBounds(5, 60, 455, 550);
        outputArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(outputArea.getBounds());
        scrollPane.setAutoscrolls(true);
        super.add(scrollPane);

        window.getExecutor().getLogger().addOutput(new TextAreaOutputDevice(outputArea));
    }

    private void runExecutor() {
        new Thread(() -> {
            BuildToolsGUI.addBuild();

            if (window.getExecutor().run()) {
                outputArea.append("\n\nThe build completed successfully!");
            } else {
                outputArea.append("\n\nAn error occurred during the build process!");
            }

            BuildToolsGUI.removeBuild();
        }).start();
    }
}
