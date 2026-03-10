package com.clientproject.launcher;

import com.clientproject.auth.AuthMode;
import java.io.IOException;
import java.nio.file.Path;

public final class LauncherSettingsService {
    private final LauncherConfigStore configStore;
    private final Path launcherRoot;

    public LauncherSettingsService(LauncherConfigStore configStore, Path launcherRoot) {
        this.configStore = configStore;
        this.launcherRoot = launcherRoot;
    }

    public LauncherProfile load() throws IOException {
        return configStore.loadOrCreate(launcherRoot);
    }

    public LauncherProfile updateRuntimeSettings(
            LauncherProfile profile,
            String javaExecutable,
            int minMemoryMb,
            int maxMemoryMb,
            Path gameDirectory,
            Path assetsDirectory,
            Path librariesDirectory) throws IOException {
        if (minMemoryMb <= 0 || maxMemoryMb < minMemoryMb) {
            throw new IllegalArgumentException("Invalid memory settings");
        }

        LauncherProfile updated = new LauncherProfile(
                profile.profileName(),
                gameDirectory,
                assetsDirectory,
                librariesDirectory,
                javaExecutable,
                minMemoryMb,
                maxMemoryMb,
                profile.username(),
                profile.accessToken(),
                profile.authMode()
        );
        configStore.save(launcherRoot.resolve(LauncherConfigStore.PROFILE_FILE), updated);
        return updated;
    }

    public LauncherProfile updateSession(LauncherProfile profile, String username, String accessToken, AuthMode authMode) throws IOException {
        LauncherProfile updated = new LauncherProfile(
                profile.profileName(),
                profile.gameDirectory(),
                profile.assetsDirectory(),
                profile.librariesDirectory(),
                profile.javaExecutable(),
                profile.minMemoryMb(),
                profile.maxMemoryMb(),
                username,
                accessToken,
                authMode
        );
        configStore.save(launcherRoot.resolve(LauncherConfigStore.PROFILE_FILE), updated);
        return updated;
    }
}
