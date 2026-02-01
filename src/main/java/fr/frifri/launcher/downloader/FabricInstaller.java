package fr.frifri.launcher.downloader;

import fr.frifri.launcher.utils.Http;
import fr.frifri.launcher.utils.FileUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.nio.file.Files;

public class FabricInstaller {

    private static final String MC_VERSION = "1.21.4";
    private static final String LOADER_VERSION = "0.18.4";

    public static void installFabric(File gameDir) {
        try {
            File librariesDir = new File(gameDir, "libraries");
            FileUtils.ensureFolder(librariesDir);

            // 1) Télécharger le profile JSON Fabric depuis l’API officielle
            String metaUrl = "https://meta.fabricmc.net/v2/versions/loader/"
                    + MC_VERSION + "/" + LOADER_VERSION + "/profile/json";

            System.out.println("Téléchargement du profile Fabric...");
            File profileJson = new File(gameDir, "fabric-profile.json");
            Http.download(metaUrl, profileJson);

            // 2) Lire le JSON
            String json = Files.readString(profileJson.toPath());
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();

            // 3) Télécharger toutes les libraries Fabric
            System.out.println("Téléchargement des libraries Fabric...");

            for (var libElement : root.getAsJsonArray("libraries")) {
                JsonObject lib = libElement.getAsJsonObject();
                String name = lib.get("name").getAsString();
                String url = lib.get("url").getAsString();

                // Convertir "group:artifact:version" en chemin
                String[] parts = name.split(":");
                String group = parts[0].replace(".", "/");
                String artifact = parts[1];
                String version = parts[2];

                String fileName = artifact + "-" + version + ".jar";
                String path = group + "/" + artifact + "/" + version + "/" + fileName;

                File outFile = new File(librariesDir, path);
                outFile.getParentFile().mkdirs();

                if (!outFile.exists()) {
                    System.out.println("Téléchargement : " + fileName);
                    Http.download(url + path, outFile);
                }
            }

            System.out.println("Fabric installé avec succès !");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur Fabric : " + e.getMessage());
        }
    }
}
