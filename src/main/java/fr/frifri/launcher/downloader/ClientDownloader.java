package fr.frifri.launcher.downloader;

import fr.frifri.launcher.utils.Http;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientDownloader {

    public static void downloadClient(File gameDir, File versionJson, String version) {
        try {
            String json = Files.readString(versionJson.toPath());

            // Regex robuste qui marche sur TOUS les JSON Mojang
            Pattern p = Pattern.compile("\"client\"\\s*:\\s*\\{[^}]*\"url\"\\s*:\\s*\"([^\"]+)\"", Pattern.DOTALL);
            Matcher m = p.matcher(json);

            if (!m.find()) {
                throw new RuntimeException("Impossible de trouver l'URL du client dans le JSON !");
            }

            String url = m.group(1);

            File dest = new File(gameDir, "versions/" + version + "/" + version + ".jar");
            dest.getParentFile().mkdirs();

            System.out.println("Téléchargement du client Minecraft...");
            Http.download(url, dest);

            System.out.println("Client Minecraft téléchargé !");
        } catch (Exception e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
