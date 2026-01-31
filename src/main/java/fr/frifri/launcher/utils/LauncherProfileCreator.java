package fr.frifri.launcher.utils;

import java.io.File;
import java.io.FileWriter;

public class LauncherProfileCreator {

    public static void ensureLauncherProfile(File gameDir) {
        try {
            File profile = new File(gameDir, "launcher_profiles.json");

            if (profile.exists()) {
                return; // déjà présent → rien à faire
            }

            // JSON minimal pour Fabric
            String json = """
            {
              "profiles": {
                "OneBlock": {
                  "name": "OneBlock",
                  "type": "custom",
                  "created": "2020-01-01T00:00:00.000Z",
                  "lastUsed": "2020-01-01T00:00:00.000Z"
                }
              },
              "selectedProfile": "OneBlock",
              "clientToken": "00000000-0000-0000-0000-000000000000"
            }
            """;

            profile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(profile)) {
                writer.write(json);
            }

            System.out.println("launcher_profiles.json créé automatiquement !");

        } catch (Exception e) {
            System.out.println("Erreur création launcher_profiles.json : " + e.getMessage());
        }
    }
}
