package fr.frifri.launcher;

import fr.frifri.launcher.auth.MinecraftSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {

    private static MinecraftSession currentSession = null;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(Main::createUI);
    }

    private static void createUI() {

        JFrame frame = new JFrame("OneBlock Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // PANEL AVEC BACKGROUND
        JPanel backgroundPanel = new JPanel() {
            Image bg = new ImageIcon(getClass().getResource("/background.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // PANEL TRANSLUCIDE PREMIUM
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fond semi-opaque (effet verre dépoli)
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Bordure douce
                g2.setColor(new Color(255, 255, 255, 40));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(420, 360));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // TITRE
        JLabel title = new JLabel("OneBlock Launcher");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));

        JLabel status = new JLabel("Mode hors-ligne (Crack)");
        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        status.setForeground(new Color(230, 230, 230));
        status.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // CHAMP PSEUDO STYLÉ
        JTextField pseudoField = new JTextField(15);
        pseudoField.setMaximumSize(new Dimension(260, 45));
        pseudoField.setHorizontalAlignment(JTextField.CENTER);
        pseudoField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pseudoField.setBackground(new Color(255, 255, 255, 220));
        pseudoField.setForeground(Color.BLACK);
        pseudoField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        // BOUTON JOUER PREMIUM
        JButton playBtn = new JButton("Jouer");
        playBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        playBtn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        playBtn.setBackground(new Color(46, 204, 113));
        playBtn.setForeground(Color.WHITE);
        playBtn.setFocusPainted(false);
        playBtn.setBorderPainted(false);
        playBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playBtn.setOpaque(true);

        // Hover
        playBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                playBtn.setBackground(new Color(39, 174, 96));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                playBtn.setBackground(new Color(46, 204, 113));
            }
        });

        // ACTION JOUER
        playBtn.addActionListener(e -> {
            String pseudo = pseudoField.getText().trim();
            if (pseudo.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Entrez un pseudo !");
                return;
            }

            currentSession = MinecraftSession.offline(pseudo);

            status.setText("Lancement du jeu...");
            playBtn.setEnabled(false);

            new Thread(() -> MinecraftLauncher.launchGame(currentSession, true)).start();
        });

        // AJOUT DES ÉLÉMENTS
        panel.add(Box.createVerticalStrut(25));
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(status);
        panel.add(Box.createVerticalStrut(25));
        panel.add(pseudoField);
        panel.add(Box.createVerticalStrut(35));
        panel.add(playBtn);

        backgroundPanel.add(panel);

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }
}
