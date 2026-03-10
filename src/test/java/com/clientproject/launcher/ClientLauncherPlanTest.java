package com.clientproject.launcher;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clientproject.auth.AuthMode;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class ClientLauncherPlanTest {
    @Test
    void buildsClasspathMainClassLaunchPlan() throws Exception {
        Path root = Files.createTempDirectory("xtweaks-plan");
        Path game = root.resolve("game");
        Path versionDir = game.resolve("versions").resolve("1.8.9");
        Files.createDirectories(versionDir);
        Files.createFile(versionDir.resolve("1.8.9.jar"));

        Path libs = root.resolve("libs");
        Files.createDirectories(libs);

        LauncherProfile profile = new LauncherProfile(
                "default", game, root.resolve("assets"), libs, "java", 512, 1024, "User", "0", AuthMode.OFFLINE_LOCAL);
        ClientLaunchConfig config = ClientLaunchConfig.fromProfile(profile, "1.8.9");

        LaunchPlan plan = new ClientLauncher().buildLaunchPlan(config, profile);

        assertTrue(plan.command().contains("-cp"));
        assertTrue(plan.command().contains("net.minecraft.client.main.Main"));
    }
}
