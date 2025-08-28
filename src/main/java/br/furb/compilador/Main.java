package br.furb.compilador;

import br.furb.compilador.componentes.LexicalError;
import br.furb.compilador.componentes.Lexico;
import br.furb.compilador.componentes.Token;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Main extends JFrame {
    private final JTextArea editorTextArea;
    private final JTextArea lineNumberArea;
    private final JTextArea messagesTextArea;
    private final JLabel statusLabel;

    private File currentFile;

    public Main() {
        super("Interface do Compilador - Equipe 1");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setMinimumSize(new Dimension(1500, 800));
        setMaximumSize(new Dimension(1500, 800));
        setResizable(false);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JToolBar toolBar = buildToolBar();
        toolBar.setFloatable(false);
        toolBar.setPreferredSize(new Dimension(1500, 70));
        add(toolBar, BorderLayout.NORTH);

        editorTextArea = new JTextArea();
        editorTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        editorTextArea.setLineWrap(false);
        editorTextArea.setTabSize(4);

        JScrollPane editorScrollPane = new JScrollPane(editorTextArea,
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

        messagesTextArea = new JTextArea();
        messagesTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        messagesTextArea.setEditable(false);
        messagesTextArea.setLineWrap(false);

        JScrollPane messagesScrollPane = new JScrollPane(messagesTextArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorScrollPane, messagesScrollPane);
        splitPane.setResizeWeight(0.7);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(8);
        add(splitPane, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        statusBar.setPreferredSize(new Dimension(1500, 25));
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(0, 8, 0, 8));
        statusBar.add(statusLabel, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);

        bindGlobalShortcuts();
    }

    private void updateLineNumbers() {
        SwingUtilities.invokeLater(() -> {
            int totalLines = editorTextArea.getLineCount();
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= totalLines; i++) {
                builder.append(i).append('\n');
            }
            lineNumberArea.setText(builder.toString());
        });
    }

    private JToolBar buildToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));

        Dimension buttonSize = new Dimension(170, 60);

        JButton btnNovo = createToolbarButton("Novo (Ctrl+N)", "/compilador/novo.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionNovo(); }
        });
        JButton btnAbrir = createToolbarButton("Abrir (Ctrl+O)", "/compilador/abrir.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionAbrir(); }
        });
        JButton btnSalvar = createToolbarButton("Salvar (Ctrl+S)", "/compilador/salvar.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionSalvar(false); }
        });
        JButton btnCopiar = createToolbarButton("Copiar (Ctrl+C)", "/compilador/copiar.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { editorTextArea.copy(); }
        });
        JButton btnColar = createToolbarButton("Colar (Ctrl+V)", "/compilador/colar.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { editorTextArea.paste(); }
        });
        JButton btnRecortar = createToolbarButton("Recortar (Ctrl+X)", "/compilador/recortar.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { editorTextArea.cut(); }
        });
        JButton btnCompilar = createToolbarButton("Compilar (F7)", "/compilador/compilar.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCompilar(); }
        });
        JButton btnEquipe = createToolbarButton("Equipe (F1)", "/compilador/equipe.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionEquipe(); }
        });

        for (JButton b : new JButton[]{btnNovo, btnAbrir, btnSalvar, btnCopiar, btnColar, btnRecortar, btnCompilar, btnEquipe}) {
            b.setPreferredSize(buttonSize);
            toolBar.add(b);
        }

        return toolBar;
    }

    private JButton createToolbarButton(String text, String iconPath, Action action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIcon(loadIcon(iconPath, text.split(" ")[0]));
        button.setFocusable(false);
        return button;
    }

    private Icon loadIcon(String resourcePath, String placeholderText) {
        try {
            java.net.URL url = Main.class.getResource(resourcePath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                    Image scaled = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
            }
        } catch (Exception ignored) {
        }
        return createPlaceholderIcon(placeholderText.substring(0, Math.min(2, placeholderText.length())).toUpperCase());
    }

    private Icon createPlaceholderIcon(String text) {
        int size = 48;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(66, 133, 244));
            g.fillRoundRect(0, 0, size, size, 12, 12);
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
            FontMetrics fm = g.getFontMetrics();
            int tw = fm.stringWidth(text);
            int th = fm.getAscent();
            g.drawString(text, (size - tw) / 2, (size + th) / 2 - 4);
        } finally {
            g.dispose();
        }
        return new ImageIcon(img);
    }

    private void bindGlobalShortcuts() {
        JRootPane root = getRootPane();
        InputMap input = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actions = root.getActionMap();

        input.put(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "novo");
        actions.put("novo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionNovo(); }
        });

        input.put(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "abrir");
        actions.put("abrir", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionAbrir(); }
        });

        input.put(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "salvar");
        actions.put("salvar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionSalvar(false); }
        });

        input.put(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "copiar");
        actions.put("copiar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { editorTextArea.copy(); }
        });

        input.put(KeyStroke.getKeyStroke('V', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "colar");
        actions.put("colar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { editorTextArea.paste(); }
        });

        input.put(KeyStroke.getKeyStroke('X', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "recortar");
        actions.put("recortar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { editorTextArea.cut(); }
        });

        input.put(KeyStroke.getKeyStroke("F7"), "compilar");
        actions.put("compilar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCompilar(); }
        });

        input.put(KeyStroke.getKeyStroke("F1"), "equipe");
        actions.put("equipe", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionEquipe(); }
        });
    }

    private void actionNovo() {
        editorTextArea.setText("");
        messagesTextArea.setText("");
        currentFile = null;
        statusLabel.setText(" ");
        updateLineNumbers();
    }

    private void actionAbrir() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Abrir arquivo de texto");
        chooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto (*.txt)", "txt"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                String content = readFile(file);
                editorTextArea.setText(content);
                editorTextArea.setCaretPosition(0);
                messagesTextArea.setText("");
                currentFile = file;
                updateStatus(file);
                updateLineNumbers();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actionSalvar(boolean forcePrompt) {
        if (currentFile == null || forcePrompt) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Salvar como");
            chooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto (*.txt)", "txt"));
            int result = chooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".txt")) {
                    file = new File(file.getParentFile(), file.getName() + ".txt");
                }
                try {
                    writeFile(file, editorTextArea.getText());
                    currentFile = file;
                    updateStatus(file);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            try {
                writeFile(currentFile, editorTextArea.getText());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actionCompilar() {
        messagesTextArea.setText(compilar());
    }

    private String compilar() {
        try {
            var lexico = new Lexico();
            lexico.setInput(editorTextArea.getText());

            var token = lexico.nextToken();
            while (token != null) {
                token = lexico.nextToken();
            }

            return "Programa compilado com sucesso";
        } catch (LexicalError e) {
            return "Linha " + getLinhaFromPos(e.getPosition()) + " " + getSimboloFromPos(e.getPosition()) + " " + e.getMessage();
        }
    }

    private int getLinhaFromPos(int pos) {
        var texto = editorTextArea.getText();
        var linhas = 1;
        for (int i = 0; i < pos; i++) {
            if (texto.charAt(i) == '\n') {
                linhas++;
            }
        }
        return linhas;
    }

    private String getSimboloFromPos(int pos) {
        var texto = editorTextArea.getText();
        var simboloInvalido = new StringBuilder();
        char atual = texto.charAt(pos);
        var tam = texto.length();
        var idx = pos;
        while (idx < tam -1 && atual != '\s' && atual != '\n') {
            simboloInvalido.append(texto.charAt(idx));
            idx++;
            atual = texto.charAt(idx);
        }

        return simboloInvalido.toString();
    }

    private void actionEquipe() {
        messagesTextArea.setText("Equipe 1:\n- Mateus Alan Koch\n- Nikolas Trapp\n- Paulo Ricardo Machado");
    }

    private void updateStatus(File file) {
        String folder = Objects.requireNonNullElse(file.getParent(), "");
        String name = file.getName();
        statusLabel.setText("Pasta: " + folder + "  |  Arquivo: " + name);
    }

    private String readFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[8192];
            int n;
            while ((n = reader.read(buf)) != -1) {
                sb.append(buf, 0, n);
            }
            return sb.toString();
        }
    }

    private void writeFile(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new Main().setVisible(true);
        });
    }
}