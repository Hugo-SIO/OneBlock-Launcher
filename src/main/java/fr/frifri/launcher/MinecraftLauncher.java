package fr.frifri.launcher;

import fr.frifri.launcher.downloader.*;
import fr.frifri.launcher.launcher.GameLauncher;
import fr.frifri.launcher.launcher.LaunchArgumentsBuilder;
import fr.frifri.launcher.utils.LauncherProfileCreator;
import fr.frifri.launcher.utils.NativeExtractor;

import java.io.File;
import java.util.List;

public class MinecraftLauncher {

    public static void launch() {

        File gameDir = new File(System.getProperty("user.home") + "/AppData/Roaming/.oneblock");

        // 1) Créer automatiquement launcher_profiles.json
        LauncherProfileCreator.ensureLauncherProfile(gameDir);

        // 2) Télécharger JSON 1.21.4
        File vanillaJson = VersionJsonDownloader.downloadVersionJson(gameDir);

        // 3) Télécharger assets, libs, client
        AssetDownloader.downloadAssets(gameDir, vanillaJson);
        LibraryDownloader.downloadLibraries(gameDir, vanillaJson);
        ClientDownloader.downloadClient(gameDir, vanillaJson, "1.21.4");

        // 4) Installer Fabric
        FabricInstaller.installFabric(gameDir);


        // EXTRACTION DES NATIVES
        File nativesDir = new File(gameDir, "natives");
        NativeExtractor.extractNatives(new File(gameDir, "libraries"), nativesDir);

        // 5) Lancer Minecraft
        List<String> command = LaunchArgumentsBuilder.buildLaunchCommand(gameDir, "1.21.4");
        GameLauncher.launchGame(command);

    }

}
