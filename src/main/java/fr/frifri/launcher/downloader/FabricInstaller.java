package fr.frifri.launcher.downloader;

import fr.frifri.launcher.utils.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class FabricInstaller {

    public static void installFabric(File gameDir) {
        try {
            FileUtils.ensureFolder(gameDir);

            File installer = new File(gameDir, "fabric-installer.jar");

            // 1) Copier le fabric-installer.jar depuis resources
            if (!installer.exists()) {
                System.out.println("Copie du Fabric Installer local...");

                try (InputStream in = FabricInstaller.class.getResourceAsStream("/fabric-installer.jar")) {
                    if (in == null) {
                        throw new RuntimeException("fabric-installer.jar introuvable dans resources !");
                    }
                    Files.copy(in, installer.toPath());
                }
            }

            // 2) Exécuter l’installer
            System.out.println("Installation de Fabric...");
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar",
                    installer.getAbsolutePath(),
                    "client",
                    "-dir", gameDir.getAbsolutePath(),
                    "-mcversion", "1.21.4",
                    "-loader", "0.18.4"
            );

            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();

            System.out.println("Fabric installé !");
        } catch (Exception e) {
            System.out.println("Erreur Fabric : " + e.getMessage());
        }
    }
}
