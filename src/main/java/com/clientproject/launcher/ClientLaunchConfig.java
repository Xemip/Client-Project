package com.clientproject.launcher;

import java.nio.file.Path;

public record ClientLaunchConfig(
        Path gameDirectory,
        Path assetsDirectory,
        Path librariesDirectory,
        String targetVersion
) {

    public static ClientLaunchConfig fromProfile(LauncherProfile profile, String targetVersion) {
        return new ClientLaunchConfig(
                profile.gameDirectory(),
                profile.assetsDirectory(),
                profile.librariesDirectory(),
                targetVersion
        );
    }
}
