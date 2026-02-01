package fr.frifri.launcher;

import fr.frifri.launcher.auth.MinecraftSession;
import fr.frifri.launcher.downloader.*;
import fr.frifri.launcher.launcher.GameLauncher;
import fr.frifri.launcher.launcher.LaunchArgumentsBuilder;
import fr.frifri.launcher.mods.ModInstaller;
import fr.frifri.launcher.utils.LauncherProfileCreator;
import fr.frifri.launcher.utils.NativeExtractor;

import java.io.File;
import java.util.List;

public class MinecraftLauncher {

    private static final String VERSION = "1.21.4";

    public static void prepare() {

        File gameDir = new File(System.getProperty("user.home") + "/AppData/Roaming/.oneblock");

        LauncherProfileCreator.ensureLauncherProfile(gameDir);

        File vanillaJson = VersionJsonDownloader.downloadVersionJson(gameDir, VERSION);


        AssetDownloader.downloadAssets(gameDir, vanillaJson);
        LibraryDownloader.downloadLibraries(gameDir, vanillaJson);
        ClientDownloader.downloadClient(gameDir, vanillaJson, VERSION);

        FabricInstaller.installFabric(gameDir);

        File nativesDir = new File(gameDir, "natives");
        NativeExtractor.extractNatives(new File(gameDir, "libraries"), nativesDir);

        System.out.println("Préparation terminée !");
    }

    public static void launchGame(MinecraftSession session, boolean offline) {
        File gameDir = new File(System.getProperty("user.home") + "/AppData/Roaming/.oneblock");

        // Vérifier si tout est prêt
        if (!isGameReady(gameDir)) {
            System.out.println("Fichiers manquants détectés. Préparation du jeu...");
            prepare();
        }

        // Installer les mods AVANT de lancer le jeu
        ModInstaller.installDefaultMods();

        // Construire la commande de lancement
        List<String> command = LaunchArgumentsBuilder.buildLaunchCommand(gameDir, VERSION, session, offline);

        // Lancer le jeu
        GameLauncher.launchGame(command);
    }


    private static boolean isGameReady(File gameDir) {
        File versionJson = new File(gameDir, "versions/" + VERSION + "/" + VERSION + ".json");
        File clientJar = new File(gameDir, "versions/" + VERSION + "/" + VERSION + ".jar");
        File librariesDir = new File(gameDir, "libraries");
        File assetsDir = new File(gameDir, "assets");
        File nativesDir = new File(gameDir, "natives");

        // Vérifier version.json
        if (!versionJson.exists()) return false;

        // Vérifier client.jar
        if (!clientJar.exists()) return false;

        // Vérifier libraries
        if (!librariesDir.exists() || librariesDir.listFiles().length == 0) return false;

        // Vérifier assets
        if (!assetsDir.exists() || assetsDir.listFiles().length == 0) return false;

        // Vérifier natives
        if (!nativesDir.exists() || nativesDir.listFiles().length == 0) return false;

        // Vérifier Fabric Loader
        File fabricLoader = new File(gameDir, "libraries/net/fabricmc/fabric-loader/0.18.4");
        if (!fabricLoader.exists()) return false;

        return true;
    }


}
