package fr.frifri.launcher.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.frifri.launcher.utils.Http;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionJsonDownloader {

    public static File downloadVersionJson(File gameDir, String version) {
        try {
            File manifest = VersionManifestDownloader.downloadManifest(gameDir);
            String json = Files.readString(manifest.toPath());
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();

            JsonArray versions = root.getAsJsonArray("versions");
            String versionUrl = null;

            for (JsonElement el : versions) {
                JsonObject obj = el.getAsJsonObject();
                if (obj.get("id").getAsString().equals(version)) {
                    versionUrl = obj.get("url").getAsString();
                    break;
                }
            }

            if (versionUrl == null)
                throw new RuntimeException("Version introuvable dans le manifest");

            File versionDir = new File(gameDir, "versions/" + version);
            versionDir.mkdirs();

            File versionJson = new File(versionDir, version + ".json");

            if (!versionJson.exists()) {
                System.out.println("Téléchargement du version.json...");
                Http.download(versionUrl, versionJson);
            }

            return versionJson;

        } catch (Exception e) {
            throw new RuntimeException("Erreur version.json : " + e.getMessage());
        }
    }
}

