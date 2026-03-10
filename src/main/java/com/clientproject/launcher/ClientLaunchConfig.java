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
    public static ClientLaunchConfig default189(Path gameDirectory, Path assetsDirectory) {
        return new ClientLaunchConfig(
                "1.8.9",
                gameDirectory,
                assetsDirectory,
                "java",
                List.of("-Xms1G", "-Xmx4G", "-XX:+UseG1GC"),
                List.of("--version", "1.8.9", "--accessToken", "${ACCESS_TOKEN}")
        );
    }
}
