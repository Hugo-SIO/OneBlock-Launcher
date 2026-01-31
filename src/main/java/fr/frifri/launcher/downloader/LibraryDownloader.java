package fr.frifri.launcher.downloader;

import fr.frifri.launcher.utils.Http;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibraryDownloader {

    public static void downloadLibraries(File gameDir, File versionJson) {
        try {
            String json = Files.readString(versionJson.toPath());

            // Regex pour extraire TOUTES les libs
            Pattern p = Pattern.compile("\"path\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"sha1\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"size\"\\s*:\\s*(\\d+)\\s*,\\s*\"url\"\\s*:\\s*\"([^\"]+)\"");
            Matcher m = p.matcher(json);

            while (m.find()) {
                String path = m.group(1);
                String url = m.group(4);

                // ❌ On ignore ASM 9.6 car Fabric installe ASM 9.9
                if (path.contains("org/ow2/asm/asm/9.6")) {
                    System.out.println("Ignoré (ASM vanilla) : " + path);
                    continue;
                }

                File dest = new File(gameDir, "libraries/" + path);
                dest.getParentFile().mkdirs();

                if (!dest.exists()) {
                    System.out.println("Téléchargement library : " + path);
                    Http.download(url, dest);
                }
            }


            System.out.println("Libraries téléchargées !");
        } catch (Exception e) {
            System.out.println("Erreur libraries : " + e.getMessage());
        }
    }
}
