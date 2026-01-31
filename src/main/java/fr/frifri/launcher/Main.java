package fr.frifri.launcher;

import fr.frifri.launcher.utils.UsernameStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(Main::createAndShowUI);
    }

    private static void createAndShowUI() {

        // Préparation du jeu en arrière-plan
        new Thread(MinecraftLauncher::prepare).start();

        JFrame frame = new JFrame("OneBlock Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        BackgroundPanel root = new BackgroundPanel("/background.png");
        root.setLayout(new BorderLayout());

        JPanel overlay = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 140));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        overlay.setOpaque(false);
        root.add(overlay, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("OneBlock Launcher");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));

        JLabel subtitle = new JLabel("Minecraft 1.21.4 • Fabric");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setForeground(new Color(220, 220, 220));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        // Champ pseudo
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        usernameField.setMaximumSize(new Dimension(220, 40));
        usernameField.setHorizontalAlignment(JTextField.CENTER);

        String saved = UsernameStorage.loadUsername();
        if (saved != null) usernameField.setText(saved);

        JLabel pseudoLabel = new JLabel("Pseudo :");
        pseudoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pseudoLabel.setForeground(Color.WHITE);
        pseudoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JButton playButton = new JButton("Jouer");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(new Color(76, 175, 80));
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        playButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playButton.setPreferredSize(new Dimension(220, 55));
        playButton.setMaximumSize(new Dimension(220, 55));

        Color normal = new Color(76, 175, 80);
        Color hover = new Color(96, 200, 100);

        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                playButton.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                playButton.setBackground(normal);
            }
        });

        JLabel statusLabel = new JLabel("Prêt");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setForeground(new Color(200, 200, 200));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        centerPanel.add(Box.createVerticalStrut(60));
        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(subtitle);

        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(pseudoLabel);
        centerPanel.add(usernameField);

        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(playButton);

        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(statusLabel);
        centerPanel.add(Box.createVerticalGlue());

        overlay.add(centerPanel, BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.setVisible(true);

        playButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Veuillez entrer un pseudo !");
                return;
            }

            UsernameStorage.saveUsername(username);

            playButton.setEnabled(false);
            playButton.setText("Lancement...");
            statusLabel.setText("Lancement du jeu...");

            new Thread(() -> {
                MinecraftLauncher.launchGame(username);
                SwingUtilities.invokeLater(() -> statusLabel.setText("Jeu lancé."));
            }).start();
        });
    }

    static class BackgroundPanel extends JPanel {
        private final Image background;

        public BackgroundPanel(String resourcePath) {
            Image img = null;
            try {
                img = new ImageIcon(getClass().getResource(resourcePath)).getImage();
            } catch (Exception e) {
                System.out.println("Impossible de charger le background : " + e.getMessage());
            }
            this.background = img;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (background != null) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
