package fr.frifri.launcher;

import fr.frifri.launcher.downloader.*;
import fr.frifri.launcher.launcher.GameLauncher;
import fr.frifri.launcher.launcher.LaunchArgumentsBuilder;
import fr.frifri.launcher.utils.LauncherProfileCreator;
import fr.frifri.launcher.utils.NativeExtractor;

import java.io.File;
import java.util.List;

public class MinecraftLauncher {

    private static final String VERSION = "1.21.4";

    public static void prepare() {

        File gameDir = new File(System.getProperty("user.home") + "/AppData/Roaming/.oneblock");

        LauncherProfileCreator.ensureLauncherProfile(gameDir);

        File vanillaJson = VersionJsonDownloader.downloadVersionJson(gameDir);

        AssetDownloader.downloadAssets(gameDir, vanillaJson);
        LibraryDownloader.downloadLibraries(gameDir, vanillaJson);
        ClientDownloader.downloadClient(gameDir, vanillaJson, VERSION);

        FabricInstaller.installFabric(gameDir);

        File nativesDir = new File(gameDir, "natives");
        NativeExtractor.extractNatives(new File(gameDir, "libraries"), nativesDir);

        System.out.println("Préparation terminée !");
    }

    public static void launchGame(String username) {
        File gameDir = new File(System.getProperty("user.home") + "/AppData/Roaming/.oneblock");
        List<String> command = LaunchArgumentsBuilder.buildLaunchCommand(gameDir, VERSION, username);
        GameLauncher.launchGame(command);
    }
}
