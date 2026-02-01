package fr.frifri.launcher.downloader;

import fr.frifri.launcher.utils.Http;

import java.io.File;

public class VersionManifestDownloader {

    private static final String MANIFEST_URL =
            "https://piston-meta.mojang.com/mc/game/version_manifest.json";

    public static File downloadManifest(File gameDir) {
        try {
            File manifestDir = new File(gameDir, "manifests");
            manifestDir.mkdirs();

            File manifestFile = new File(manifestDir, "version_manifest.json");

            if (!manifestFile.exists()) {
                System.out.println("Téléchargement du manifest Minecraft...");
                Http.download(MANIFEST_URL, manifestFile);
            }

            return manifestFile;

        } catch (Exception e) {
            throw new RuntimeException("Impossible de télécharger le manifest : " + e.getMessage());
        }
    }
}
