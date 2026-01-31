package fr.frifri.launcher.utils;

import java.io.*;
import java.nio.file.*;
import java.util.jar.*;

public class NativeExtractor {

    public static void extractNatives(File librariesDir, File nativesDir) {
        nativesDir.mkdirs();

        try {
            Files.walk(librariesDir.toPath())
                    .filter(path -> path.toString().contains("natives-windows") && path.toString().endsWith(".jar"))
                    .forEach(path -> extractJar(path.toFile(), nativesDir));
        } catch (Exception e) {
            System.out.println("Erreur extraction natives : " + e.getMessage());
        }
    }

    private static void extractJar(File jarFile, File outputDir) {
        try (JarFile jar = new JarFile(jarFile)) {
            jar.stream().forEach(entry -> {
                if (!entry.isDirectory() && entry.getName().endsWith(".dll")) {
                    File out = new File(outputDir, new File(entry.getName()).getName());
                    try (InputStream is = jar.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(out)) {
                        is.transferTo(fos);
                    } catch (Exception ignored) {}
                }
            });
        } catch (Exception ignored) {}
    }
}
