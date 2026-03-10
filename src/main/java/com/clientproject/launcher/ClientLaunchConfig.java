package com.clientproject.launcher;

public class ClientLaunchConfig {

    private final LauncherProfile profile;
    private final String gameDir;

    public ClientLaunchConfig(LauncherProfile profile, String gameDir) {
        this.profile = profile;
        this.gameDir = gameDir;
    }

    public static ClientLaunchConfig fromProfile(LauncherProfile profile, String gameDir) {
        return new ClientLaunchConfig(profile, gameDir);
    }

    public LauncherProfile getProfile() {
        return profile;
    }

    public String getGameDir() {
        return gameDir;
    }
}
