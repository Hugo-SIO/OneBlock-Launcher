package fr.frifri.launcher.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.frifri.launcher.utils.Http;

import java.io.File;
import java.nio.file.Files;

public class LibraryDownloader {

    public static void downloadLibraries(File gameDir, File versionJson) {
        try {
            String json = Files.readString(versionJson.toPath());
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();

            JsonArray libraries = root.getAsJsonArray("libraries");
            File librariesDir = new File(gameDir, "libraries");

            for (JsonElement el : libraries) {
                JsonObject lib = el.getAsJsonObject();

                if (!lib.has("downloads")) continue;
                if (!lib.getAsJsonObject("downloads").has("artifact")) continue;

                JsonObject artifact = lib.getAsJsonObject("downloads").getAsJsonObject("artifact");

                String url = artifact.get("url").getAsString();
                String path = artifact.get("path").getAsString();

                // Éviter les doublons ASM (Fabric fournit ASM 9.9)
                if (path.contains("org/ow2/asm/asm")) {
                    System.out.println("Skip ASM (évite doublon avec Fabric)");
                    continue;
                }

                File out = new File(librariesDir, path);
                out.getParentFile().mkdirs();

                if (!out.exists()) {
                    System.out.println("Téléchargement lib : " + path);
                    Http.download(url, out);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur libraries : " + e.getMessage());
        }
    }
}

