package fr.frifri.launcher.mods;

import fr.frifri.launcher.utils.DownloadUtils;

import java.io.File;

public class ModInstaller {

    // Liste des mods à télécharger (nom + URL)
    private static final String[][] MODS = {
            {"sodium.jar", "https://cdn.modrinth.com/data/AANobbMI/versions/c3YkZvne/sodium-fabric-0.6.13%2Bmc1.21.4.jar"},
            {"iris.jar", "https://cdn.modrinth.com/data/YL57xq9U/versions/Ca054sTe/iris-fabric-1.8.8%2Bmc1.21.4.jar"},
            {"zoom.jar", "https://cdn.modrinth.com/data/w7ThoJFB/versions/hFQfRvX1/zoomify-2.15.1%2B1.21.5.jar"},
            {"fabricKotlin.jar", "https://cdn.modrinth.com/data/Ha28R6CL/versions/N6D3uiZF/fabric-language-kotlin-1.13.8%2Bkotlin.2.3.0.jar"},
            {"yacl.jar", "https://cdn.modrinth.com/data/1eAoo2KR/versions/kcTd0BNZ/yet_another_config_lib_v3-3.8.2%2B1.21.4-fabric.jar"},
            {"fabricAPI.jar", "https://cdn.modrinth.com/data/P7dR8mSH/versions/p96k10UR/fabric-api-0.119.4%2B1.21.4.jar"}

    };

    public static void installDefaultMods() {
        try {
            // Dossier .oneblock
            File oneblockDir = new File(System.getenv("APPDATA"), ".oneblock");
            oneblockDir.mkdirs();

            // Dossier mods interne
            File modsFolder = new File(oneblockDir, "mods");
            modsFolder.mkdirs();

            // Télécharger chaque mod individuellement
            for (String[] mod : MODS) {
                File modFile = new File(modsFolder, mod[0]);

                if (!modFile.exists()) {
                    System.out.println("Téléchargement du mod : " + mod[0]);
                    DownloadUtils.downloadFile(mod[1], modFile);
                } else {
                    System.out.println("Déjà installé : " + mod[0]);
                }
            }

            System.out.println("Tous les mods sont installés.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'installation des mods.");
        }
    }
}
