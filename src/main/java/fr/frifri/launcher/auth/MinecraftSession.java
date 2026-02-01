package fr.frifri.launcher.auth;

import java.util.UUID;

public class MinecraftSession {

    public final String username;
    public final String uuid;
    public final String accessToken;

    public MinecraftSession(String username, String uuid, String accessToken) {
        this.username = username;
        this.uuid = uuid;
        this.accessToken = accessToken;
    }

    public static MinecraftSession offline(String username) {
        String uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes()).toString();
        return new MinecraftSession(username, uuid, "0");
    }
}
