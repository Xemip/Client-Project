package com.clientproject.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clientproject.auth.AuthMode;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class ClientLauncherTest {
    @Test
    void buildsJavaCommandFor189Client() throws Exception {
        Path root = Files.createTempDirectory("xtweaks-launcher");
        Path game = root.resolve("game");
        Path versionDir = game.resolve("versions").resolve("1.8.9");
        Files.createDirectories(versionDir);
        Files.createFile(versionDir.resolve("1.8.9.jar"));

        LauncherProfile profile = new LauncherProfile(
                "default", game, root.resolve("assets"), root.resolve("libs"), "java", 1024, 2048, "Player", "", AuthMode.OFFLINE_LOCAL);

        ClientLaunchConfig config = ClientLaunchConfig.fromProfile(profile, "1.8.9");
        LaunchPlan plan = new ClientLauncher().buildLaunchPlan(config, profile);

        assertEquals("java", plan.command().get(0));
        assertTrue(plan.command().contains("net.minecraft.client.main.Main"));
        assertTrue(plan.command().contains("--version"));
        assertTrue(plan.command().contains("--userType"));
        assertTrue(plan.command().contains("offline_local"));
        assertTrue(plan.command().contains("-XX:MaxGCPauseMillis=40"));
    }
}