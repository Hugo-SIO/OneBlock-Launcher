package fr.frifri.launcher.downloader;

import fr.frifri.launcher.utils.Http;
import fr.frifri.launcher.utils.FileUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

public class AssetDownloader {

    public static void downloadAssets(File gameDir, File versionJson) {
        try {
            // Lire le version.json
            String json = Files.readString(versionJson.toPath());
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();

            // Récupérer l'URL de l'asset index
            JsonObject assetIndexInfo = root.getAsJsonObject("assetIndex");
            String indexUrl = assetIndexInfo.get("url").getAsString();
            String indexId = assetIndexInfo.get("id").getAsString();

            // Dossier assets
            File assetsDir = new File(gameDir, "assets");
            File indexesDir = new File(assetsDir, "indexes");
            File objectsDir = new File(assetsDir, "objects");

            indexesDir.mkdirs();
            objectsDir.mkdirs();

            // Télécharger l'index
            File indexFile = new File(indexesDir, indexId + ".json");
            Http.download(indexUrl, indexFile);

            System.out.println("Index téléchargé : " + indexId);

            // Lire l'index
            String indexJson = Files.readString(indexFile.toPath());
            JsonObject indexRoot = JsonParser.parseString(indexJson).getAsJsonObject();
            JsonObject objects = indexRoot.getAsJsonObject("objects");

            // Télécharger chaque asset
            for (Map.Entry<String, com.google.gson.JsonElement> entry : objects.entrySet()) {
                JsonObject obj = entry.getValue().getAsJsonObject();
                String hash = obj.get("hash").getAsString();
                String sub = hash.substring(0, 2);

                File outFile = new File(objectsDir, sub + "/" + hash);
                outFile.getParentFile().mkdirs();

                if (!outFile.exists()) {
                    String assetUrl = "https://resources.download.minecraft.net/" + sub + "/" + hash;
                    Http.download(assetUrl, outFile);
                    System.out.println("Asset téléchargé : " + entry.getKey());
                }
            }

            System.out.println("Tous les assets sont téléchargés !");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur assets : " + e.getMessage());
        }
    }
}
