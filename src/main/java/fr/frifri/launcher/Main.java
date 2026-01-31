package fr.frifri.launcher;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        // Préparation du jeu (téléchargements, fabric, natives)
        MinecraftLauncher.prepare();

        // UI du launcher
        JFrame frame = new JFrame("OneBlock Launcher");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JButton playButton = new JButton("Jouer");
        playButton.setFont(new Font("Arial", Font.BOLD, 24));

        playButton.addActionListener(e -> {
            playButton.setEnabled(false);
            playButton.setText("Lancement...");
            MinecraftLauncher.launchGame();
        });

        frame.add(playButton, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
