package com.clientproject.launcher;

import java.nio.file.Path;
import java.util.List;

/**
 * Launch configuration for a standalone Minecraft 1.8.9 PvP/performance client runtime.
 */
public record ClientLaunchConfig(
        String minecraftVersion,
        Path gameDirectory,
        Path assetsDirectory,
        String javaExecutable,
        List<String> jvmArgs,
        List<String> gameArgs
) {
    public static ClientLaunchConfig fromProfile(LauncherProfile profile, String minecraftVersion) {
        return new ClientLaunchConfig(
                minecraftVersion,
                profile.gameDirectory(),
                profile.assetsDirectory(),
                profile.javaExecutable(),
                List.of(
                        "-Xms" + profile.minMemoryMb() + "M",
                        "-Xmx" + profile.maxMemoryMb() + "M",
                        "-XX:+UseG1GC",
                        "-XX:+UnlockExperimentalVMOptions",
                        "-XX:G1NewSizePercent=20",
                        "-XX:G1ReservePercent=20",
                        "-XX:MaxGCPauseMillis=40"),
                List.of(
                        "--version", minecraftVersion,
                        "--gameDir", profile.gameDirectory().toString(),
                        "--assetsDir", profile.assetsDirectory().toString(),
                        "--username", profile.username(),
                        "--accessToken", profile.accessToken().isBlank() ? "0" : profile.accessToken())
        );
    }
}
