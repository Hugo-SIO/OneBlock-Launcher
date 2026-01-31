package fr.frifri.launcher.downloader;

import fr.frifri.launcher.utils.Http;
import fr.frifri.launcher.utils.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssetDownloader {

    public static void downloadAssets(File gameDir, File versionJson) {
        try {
            String json = Files.readString(versionJson.toPath());

            Pattern p = Pattern.compile("\"url\":\"(https://[^\"]+assetindex[^\"]+)\"");
            Matcher m = p.matcher(json);

            if (!m.find()) return;

            String url = m.group(1);

            File assetIndex = new File(gameDir, "assets/index.json");
            FileUtils.ensureFolder(assetIndex.getParentFile());

            Http.download(url, assetIndex);

            System.out.println("Assets téléchargés !");
        } catch (Exception e) {
            System.out.println("Erreur assets : " + e.getMessage());
        }
    }
}
