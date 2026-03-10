package com.clientproject.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class LauncherConfigStoreTest {
    @Test
    void createsAndPersistsProfile() throws Exception {
        Path root = Files.createTempDirectory("xtweaks-profile");
        LauncherConfigStore store = new LauncherConfigStore();

        LauncherProfile profile = store.loadOrCreate(root);
        assertTrue(Files.exists(root.resolve(LauncherConfigStore.PROFILE_FILE)));
        assertEquals("default", profile.profileName());

        LauncherProfile updated = new LauncherProfile(
                profile.profileName(),
                profile.gameDirectory(),
                profile.assetsDirectory(),
                profile.librariesDirectory(),
                profile.javaExecutable(),
                profile.minMemoryMb(),
                profile.maxMemoryMb(),
                "Tester",
                "TOKEN",
                profile.authMode());

        store.save(root.resolve(LauncherConfigStore.PROFILE_FILE), updated);
        LauncherProfile loaded = store.loadOrCreate(root);
        assertEquals("Tester", loaded.username());
    }
}
