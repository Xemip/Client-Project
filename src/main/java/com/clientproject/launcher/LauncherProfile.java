package com.clientproject.launcher;

import com.clientproject.auth.AuthMode;
import java.nio.file.Path;

public record LauncherProfile(
        String profileName,
        Path gameDirectory,
        Path assetsDirectory,
        Path librariesDirectory,
        String javaExecutable,
        int minMemoryMb,
        int maxMemoryMb,
        String username,
        String accessToken,
        AuthMode authMode
) {
    public static LauncherProfile defaultProfile(Path root) {
        Path mcRoot = Path.of(System.getProperty("user.home"), ".minecraft");
        return new LauncherProfile(
                "default",
                root,
                mcRoot.resolve("assets"),
                mcRoot.resolve("libraries"),
                "java",
                1024,
                3072,
                "Player",
                "",
                AuthMode.OFFLINE_LOCAL
        );
    }
}
