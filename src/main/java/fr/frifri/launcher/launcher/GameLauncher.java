package fr.frifri.launcher.launcher;

import java.util.List;

public class GameLauncher {

    public static void launchGame(List<String> command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            pb.start();
        } catch (Exception e) {
            System.out.println("Erreur lancement : " + e.getMessage());
        }
    }
}
