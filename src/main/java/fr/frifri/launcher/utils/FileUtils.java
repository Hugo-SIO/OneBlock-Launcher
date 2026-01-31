package fr.frifri.launcher.utils;

import java.io.File;

public class FileUtils {

    public static void ensureFolder(File folder) {
        if (!folder.exists()) folder.mkdirs();
    }
}
