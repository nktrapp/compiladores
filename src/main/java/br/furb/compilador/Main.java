package br.furb.compilador;

import br.furb.compilador.componentes.*;
import br.furb.compilador.view.ConsolePanel;
import br.furb.compilador.view.EditorPanel;
import br.furb.compilador.view.Toolbar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static br.furb.compilador.componentes.ScannerConstants.SCANNER_ERROR;
import static br.furb.compilador.util.Constantes.CLASSES;
import static br.furb.compilador.util.Constantes.ESPACO;

public class Main extends JFrame {
    private final EditorPanel editorPanel;
    private final ConsolePanel consolePanel;
    private final JLabel statusLabel;

    private File currentFile;

    public Main() {
        super("Interface do Compilador - Equipe 1");

        editorPanel = new EditorPanel();
        consolePanel = new ConsolePanel();

        setSize(1500, 800);
        setMinimumSize(new Dimension(1500, 800));
        setMaximumSize(new Dimension(1500, 800));
        setResizable(false);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        var toolBar = new Toolbar(
                e -> actionNovo(),
                e -> actionAbrir(),
                e -> actionSalvar(false),
                e -> editorPanel.getEditorTextArea().copy(),
                e -> editorPanel.getEditorTextArea().paste(),
                e -> editorPanel.getEditorTextArea().cut(),
                e -> actionCompilar(),
                e -> actionEquipe()
        );
        add(toolBar, BorderLayout.NORTH);

        var splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, consolePanel);
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

    private void bindGlobalShortcuts() {
        JRootPane root = getRootPane();
        InputMap input = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actions = root.getActionMap();

        input.put(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "novo");
        actions.put("novo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionNovo();
            }
        });

        input.put(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "abrir");
        actions.put("abrir", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionAbrir();
            }
        });

        input.put(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "salvar");
        actions.put("salvar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionSalvar(false);
            }
        });

        input.put(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "copiar");
        actions.put("copiar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorPanel.getEditorTextArea().copy();
            }
        });

        input.put(KeyStroke.getKeyStroke('V', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "colar");
        actions.put("colar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorPanel.getEditorTextArea().paste();
            }
        });

        input.put(KeyStroke.getKeyStroke('X', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "recortar");
        actions.put("recortar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorPanel.getEditorTextArea().cut();
            }
        });

        input.put(KeyStroke.getKeyStroke("F7"), "compilar");
        actions.put("compilar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionCompilar();
            }
        });

        input.put(KeyStroke.getKeyStroke("F1"), "equipe");
        actions.put("equipe", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEquipe();
            }
        });
    }

    private void actionNovo() {
        editorPanel.setText("");
        consolePanel.setText("");
        currentFile = null;
        statusLabel.setText(" ");
        editorPanel.updateLineNumbers();
    }

    private void actionAbrir() {
        var fileChooser = new JFileChooser();
        var filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (var reader = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile), StandardCharsets.UTF_8))) {
                var content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                editorPanel.setText(content.toString());
                editorPanel.setCaretPosition(0);
                consolePanel.setText("");
                currentFile = selectedFile;
                updateStatus(selectedFile);
                editorPanel.updateLineNumbers();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actionSalvar(boolean forcePrompt) {
        if (currentFile == null || forcePrompt) {
            var fileChooser = new JFileChooser();
            var filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
                }
                try (var writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToSave), StandardCharsets.UTF_8))) {
                    writer.write(editorPanel.getText());
                    currentFile = fileToSave;
                    updateStatus(fileToSave);
                    consolePanel.setText("");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            try (var writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentFile), StandardCharsets.UTF_8))) {
                writer.write(editorPanel.getText());
                consolePanel.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actionCompilar() {
        var codigoFonte = editorPanel.getText();
        var lexico = new Lexico();
        var sintatico = new Sintatico();
        var semantico = new Semantico();
        lexico.setInput(new StringReader(codigoFonte));
        try {
            sintatico.parse(lexico, semantico);
            consolePanel.setText("Programa compilado com sucesso.");
        } catch (LexicalError e) {
            handleLexicalError(e);
        } catch (SyntaticError e) {
            handleSyntaticError(e);
        } catch (SemanticError e) {
            handleSemanticError(e);
        }
    }

    private void handleSemanticError(SemanticError e) {
        consolePanel.setText("Aiin zé da mangãnn.");
    }

    private void handleSyntaticError(SyntaticError e) {
        var erro = new StringBuilder();
        erro.append("Linha: ");
        erro.append(getLinhaFromPos(e.getPosition()));
        erro.append(" encontrado: ");

        var token = getSimboloFromPos(e.getPosition());
        token = token.startsWith("\"") ? CLASSES.get(Constants.t_cstring) : token;
        if ("$".equals(token)) {
            token = "EOF";
        }

        erro.append(token).append(" ");
        erro.append(e.getMessage());
        consolePanel.setText(erro.toString());

    }

    private void handleLexicalError(LexicalError e) {
        var erro = new StringBuilder();
        erro.append("Linha: ");
        erro.append(getLinhaFromPos(e.getPosition()));
        erro.append(" ");
        if (!SCANNER_ERROR[2].equals(e.getMessage())) {
            erro.append(getSimboloFromPos(e.getPosition()));
            erro.append(" ");
        }
        erro.append(e.getMessage());
        consolePanel.setText(erro.toString());
    }

    public static String stripTo(String str, int tam) {
        if (str.length() >= tam) {
            return str.substring(0, tam);
        }

        var count = str.length() - 1;
        var spaces = " ".repeat(Math.max(0, tam - (count - 1)));

        return str + spaces;
    }

    private void actionEquipe() {
        var message = new StringBuilder();
        message.append("Equipe 1:\n");
        message.append(" - Mateus Alan Koch\n");
        message.append(" - Nikolas Trapp\n");
        message.append(" - Paulo Ricardo Machado");
        consolePanel.setText(message.toString());
    }

    private void updateStatus(File file) {
        String folder = Objects.requireNonNullElse(file.getParent(), "");
        String name = file.getName();
        statusLabel.setText("Pasta: " + folder + "  |  Arquivo: " + name);
    }

    private int getLinhaFromPos(int pos) {
        var texto = editorPanel.getText();
        var linhas = 1;
        for (int i = 0; i < pos; i++) {
            if (texto.charAt(i) == '\n') {
                linhas++;
            }
        }
        return linhas;
    }

    private String getSimboloFromPos(int pos) {
        var texto = editorPanel.getText();
        var simboloInvalido = new StringBuilder();
        int tam = texto.length();

        if (pos < 0 || pos >= tam) {
            return "";
        }

        int idx = pos;
        char atual = texto.charAt(idx);
        simboloInvalido.append(atual);

        while (++idx < tam) {
            atual = texto.charAt(idx);
            if (atual == ESPACO || atual == '\n') {
                break;
            }
            simboloInvalido.append(atual);
        }

        return simboloInvalido.toString();
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