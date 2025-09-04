package br.furb.view;

import javax.swing.*;
import java.awt.*;

public class ConsolePanel extends JPanel {
    private final JTextArea messagesTextArea;

    public ConsolePanel() {
        setLayout(new BorderLayout());

        messagesTextArea = new JTextArea();
        messagesTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        messagesTextArea.setEditable(false);
        messagesTextArea.setLineWrap(false);

        var messagesScrollPane = new JScrollPane(messagesTextArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        add(messagesScrollPane, BorderLayout.CENTER);
    }

    public void setText(String text) {
        messagesTextArea.setText(text);
    }
}
