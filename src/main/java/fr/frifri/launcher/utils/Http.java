package fr.frifri.launcher.utils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class Http {

    public static void download(String url, File dest) throws IOException {
        dest.getParentFile().mkdirs();
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, dest.toPath());
        }
    }
}
