package fr.frifri.launcher.utils;

import java.io.*;

public class UsernameStorage {

    private static final File FILE = new File(System.getProperty("user.home") + "/AppData/Roaming/.oneblock/username.txt");

    public static void saveUsername(String username) {
        try {
            FILE.getParentFile().mkdirs();
            try (FileWriter fw = new FileWriter(FILE)) {
                fw.write(username);
            }
        } catch (Exception ignored) {}
    }

    public static String loadUsername() {
        try {
            if (!FILE.exists()) return null;
            return new String(java.nio.file.Files.readAllBytes(FILE.toPath())).trim();
        } catch (Exception e) {
            return null;
        }
    }
}
