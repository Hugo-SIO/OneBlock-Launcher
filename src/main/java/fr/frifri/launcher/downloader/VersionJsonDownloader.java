package fr.frifri.launcher.downloader;

import fr.frifri.launcher.utils.Http;

import java.io.File;

public class VersionJsonDownloader {

    public static File downloadVersionJson(File gameDir) {
        try {
            File jsonFile = new File(gameDir, "1.21.4.json");

            // ✔️ Si le fichier existe déjà → on ne télécharge pas
            if (jsonFile.exists()) {
                System.out.println("Version JSON déjà présent !");
                return jsonFile;
            }

            // URL officielle Mojang
            String url = "https://piston-meta.mojang.com/v1/packages/a7e5a6024bfd3cd614625aa05629adf760020304/1.21.4.json";

            jsonFile.getParentFile().mkdirs();
            Http.download(url, jsonFile);

            System.out.println("Version JSON téléchargée !");
            return jsonFile;

        } catch (Exception e) {
            throw new RuntimeException("Impossible de télécharger la version : " + e.getMessage());
        }
    }

}
