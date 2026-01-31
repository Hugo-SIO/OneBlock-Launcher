package fr.frifri.launcher.downloader;

import fr.frifri.launcher.utils.Http;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionJsonDownloader {

    public static File downloadVersionJson(File gameDir) {
        try {
            File jsonFile = new File(gameDir, "1.21.4.json");

            if (jsonFile.exists()) {
                System.out.println("Version JSON déjà présent !");
                return jsonFile;
            }

            // 1) Télécharger le manifest officiel
            String manifestUrl = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";
            File manifestFile = new File(gameDir, "version_manifest.json");
            Http.download(manifestUrl, manifestFile);

            String manifest = Files.readString(manifestFile.toPath());

            // 2) Trouver l’URL de la version 1.21.4
            Pattern p = Pattern.compile("\"id\"\\s*:\\s*\"1.21.4\"[^{]*\"url\"\\s*:\\s*\"([^\"]+)\"");
            Matcher m = p.matcher(manifest);

            if (!m.find()) {
                throw new RuntimeException("Impossible de trouver l’URL du JSON 1.21.4 dans le manifest !");
            }

            String versionUrl = m.group(1);

            // 3) Télécharger le JSON de version
            Http.download(versionUrl, jsonFile);

            System.out.println("Version JSON téléchargée !");
            return jsonFile;

        } catch (Exception e) {
            throw new RuntimeException("Impossible de télécharger la version : " + e.getMessage());
        }
    }


}
