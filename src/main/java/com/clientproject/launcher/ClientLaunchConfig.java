package com.clientproject.launcher;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientLaunchConfig {

    private final LauncherProfile profile;
    private final Path gameDir;

    public ClientLaunchConfig(LauncherProfile profile, Path gameDir) {
        this.profile = profile;
        this.gameDir = gameDir;
    }

    public static ClientLaunchConfig fromProfile(LauncherProfile profile, String gameDir) {
        return new ClientLaunchConfig(profile, Paths.get(gameDir));
    }

    public LauncherProfile getProfile() {
        return profile;
    }

    public Path getGameDir() {
        return gameDir;
    }
}
