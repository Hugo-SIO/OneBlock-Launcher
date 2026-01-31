package fr.frifri.launcher.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LaunchArgumentsBuilder {

    public static List<String> buildLaunchCommand(File gameDir, String version) {

        List<String> command = new ArrayList<>();

        command.add("java");
        command.add("-Xmx2G");
        command.add("-Xms1G");

        File nativesDir = new File(gameDir, "natives");
        command.add("-Djava.library.path=" + nativesDir.getAbsolutePath());

        StringBuilder classpath = new StringBuilder();

        addAllJars(new File(gameDir, "libraries"), classpath);
        addAllJars(new File(gameDir, "versions/" + version), classpath);
        addAllJars(new File(gameDir, "versions/1.21.4-fabric"), classpath);

        command.add("-cp");
        command.add(classpath.toString());

        command.add("net.fabricmc.loader.impl.launch.knot.KnotClient");

        command.add("--launchTarget");
        command.add("client");

        command.add("--gameDir");
        command.add(gameDir.getAbsolutePath());

        command.add("--assetsDir");
        command.add(new File(gameDir, "assets").getAbsolutePath());

        command.add("--version");
        command.add(version);

        return command;
    }

    private static void addAllJars(File folder, StringBuilder classpath) {
        if (!folder.exists()) return;

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                addAllJars(file, classpath);
            } else if (file.getName().endsWith(".jar")) {

                if (file.getName().contains("natives")) continue;

                // Éviter le conflit ASM
                if (file.getAbsolutePath().contains("org/ow2/asm/asm/9.6")) continue;

                if (classpath.length() > 0) classpath.append(File.pathSeparator);
                classpath.append(file.getAbsolutePath());
            }
        }
    }


}
