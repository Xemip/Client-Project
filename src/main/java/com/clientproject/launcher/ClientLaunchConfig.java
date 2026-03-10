package com.clientproject.launcher;

public class ClientLaunchConfig {

    private String gameDir;
    private String version;

    public ClientLaunchConfig(String gameDir, String version) {
        this.gameDir = gameDir;
        this.version = version;
    }

    public String getGameDir() {
        return gameDir;
    }

    public String getVersion() {
        return version;
    }
}
