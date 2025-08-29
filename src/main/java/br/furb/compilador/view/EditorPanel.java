package br.furb.compilador.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class EditorPanel extends JPanel {
    private final JTextArea editorTextArea;
    private final JTextArea lineNumberArea;

    public EditorPanel() {
        setLayout(new BorderLayout());

        editorTextArea = new JTextArea();
        editorTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        editorTextArea.setLineWrap(false);
        editorTextArea.setTabSize(4);

        var editorScrollPane = new JScrollPane(editorTextArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        lineNumberArea = new JTextArea("1\n");
        lineNumberArea.setEditable(false);
        lineNumberArea.setFocusable(false);
        lineNumberArea.setFont(editorTextArea.getFont());
        lineNumberArea.setBackground(new Color(245, 245, 245));
        lineNumberArea.setBorder(new EmptyBorder(0, 8, 0, 8));
        editorScrollPane.setRowHeaderView(lineNumberArea);

        editorTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateLineNumbers(); }

            @Override
            public void removeUpdate(DocumentEvent e) { updateLineNumbers(); }

            @Override
            public void changedUpdate(DocumentEvent e) { updateLineNumbers(); }
        });
        editorTextArea.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { updateLineNumbers(); }
        });

        add(editorScrollPane, BorderLayout.CENTER);
        updateLineNumbers();
    }

    public JTextArea getEditorTextArea() {
        return editorTextArea;
    }

    public void updateLineNumbers() {
        SwingUtilities.invokeLater(() -> {
            int totalLines = editorTextArea.getLineCount();
            var builder = new StringBuilder();
            for (int i = 1; i <= totalLines; i++) {
                builder.append(i).append('\n');
            }
            lineNumberArea.setText(builder.toString());
        });
    }

    public String getText() {
        return editorTextArea.getText();
    }

    public void setText(String text) {
        editorTextArea.setText(text);
    }

    public void setCaretPosition(int position) {
        editorTextArea.setCaretPosition(position);
    }
}
