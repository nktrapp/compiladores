package br.furb.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * A toolbar with buttons for common actions.
 */
public class Toolbar extends JToolBar {

    public Toolbar(ActionListener... actions) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));
        setFloatable(false);
        setPreferredSize(new Dimension(1500, 70));

        var buttonSize = new Dimension(170, 60);

        var btnNovo = createToolbarButton("Novo (Ctrl+N)", "/icons/novo.png", actions[0]);
        var btnAbrir = createToolbarButton("Abrir (Ctrl+O)", "/icons/abrir.png", actions[1]);
        var btnSalvar = createToolbarButton("Salvar (Ctrl+S)", "/icons/salvar.png", actions[2]);
        var btnCopiar = createToolbarButton("Copiar (Ctrl+C)", "/icons/copiar.png", actions[3]);
        var btnColar = createToolbarButton("Colar (Ctrl+V)", "/icons/colar.png", actions[4]);
        var btnRecortar = createToolbarButton("Recortar (Ctrl+X)", "/icons/recortar.png", actions[5]);
        var btnCompilar = createToolbarButton("Compilar (F7)", "/icons/compilar.png", actions[6]);
        var btnEquipe = createToolbarButton("Equipe (F1)", "/icons/equipe.png", actions[7]);

        for (var b : new JButton[]{btnNovo, btnAbrir, btnSalvar, btnCopiar, btnColar, btnRecortar, btnCompilar, btnEquipe}) {
            b.setPreferredSize(buttonSize);
            add(b);
        }
    }

    private JButton createToolbarButton(String text, String iconPath, ActionListener action) {
        var button = new JButton(text);
        button.addActionListener(action);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIcon(loadIcon(iconPath, text.split(" ")[0]));
        button.setFocusable(false);
        return button;
    }

    private Icon loadIcon(String resourcePath, String placeholderText) {
        try {
            var url = getClass().getResource(resourcePath);
            if (url != null) {
                var icon = new ImageIcon(url);
                if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                    var scaled = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
            }
        } catch (Exception ignored) {
        }
        return createPlaceholderIcon(placeholderText.substring(0, Math.min(2, placeholderText.length())).toUpperCase());
    }

    private Icon createPlaceholderIcon(String text) {
        int size = 48;
        var img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        var g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(66, 133, 244));
            g.fillRoundRect(0, 0, size, size, 12, 12);
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
            var fm = g.getFontMetrics();
            int tw = fm.stringWidth(text);
            int th = fm.getAscent();
            g.drawString(text, (size - tw) / 2, (size + th) / 2 - 4);
        } finally {
            g.dispose();
        }
        return new ImageIcon(img);
    }
}
