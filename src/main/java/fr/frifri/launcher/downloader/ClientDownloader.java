package fr.frifri.launcher.downloader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.frifri.launcher.utils.Http;

import java.io.File;
import java.nio.file.Files;

public class ClientDownloader {

    public static void downloadClient(File gameDir, File versionJson, String version) {
        try {
            String json = Files.readString(versionJson.toPath());
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();

            String clientUrl = root.getAsJsonObject("downloads")
                    .getAsJsonObject("client")
                    .get("url").getAsString();

            File clientFile = new File(gameDir, "versions/" + version + "/" + version + ".jar");

            if (!clientFile.exists()) {
                System.out.println("Téléchargement du client Minecraft...");
                Http.download(clientUrl, clientFile);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur client : " + e.getMessage());
        }
    }
}

