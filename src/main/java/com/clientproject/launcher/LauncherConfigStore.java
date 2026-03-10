package com.clientproject.launcher;

import com.clientproject.auth.AuthMode;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class LauncherConfigStore {
    public static final String PROFILE_FILE = "launcher-profile.properties";

    public LauncherProfile loadOrCreate(Path launcherRoot) throws IOException {
        Files.createDirectories(launcherRoot);
        Path profilePath = launcherRoot.resolve(PROFILE_FILE);
        if (!Files.exists(profilePath)) {
            LauncherProfile profile = LauncherProfile.defaultProfile(launcherRoot);
            save(profilePath, profile);
            return profile;
        }
        return load(profilePath);
    }

    public void save(Path profilePath, LauncherProfile profile) throws IOException {
        saveInternal(profilePath, profile);
    }

    private LauncherProfile load(Path profilePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(profilePath)) {
            properties.load(in);
        }

        return new LauncherProfile(
                properties.getProperty("profileName", "default"),
                Path.of(properties.getProperty("gameDirectory")),
                Path.of(properties.getProperty("assetsDirectory")),
                Path.of(properties.getProperty("librariesDirectory")),
                properties.getProperty("javaExecutable", "java"),
                Integer.parseInt(properties.getProperty("minMemoryMb", "1024")),
                Integer.parseInt(properties.getProperty("maxMemoryMb", "4096")),
                properties.getProperty("username", "Player"),
                properties.getProperty("accessToken", ""),
                AuthMode.valueOf(properties.getProperty("authMode", AuthMode.OFFLINE_LOCAL.name()))
        );
    }

    private void saveInternal(Path profilePath, LauncherProfile profile) throws IOException {
        Files.createDirectories(profilePath.getParent());
        Properties properties = new Properties();
        properties.setProperty("profileName", profile.profileName());
        properties.setProperty("gameDirectory", profile.gameDirectory().toString());
        properties.setProperty("assetsDirectory", profile.assetsDirectory().toString());
        properties.setProperty("librariesDirectory", profile.librariesDirectory().toString());
        properties.setProperty("javaExecutable", profile.javaExecutable());
        properties.setProperty("minMemoryMb", String.valueOf(profile.minMemoryMb()));
        properties.setProperty("maxMemoryMb", String.valueOf(profile.maxMemoryMb()));
        properties.setProperty("username", profile.username());
        properties.setProperty("accessToken", profile.accessToken());
        properties.setProperty("authMode", profile.authMode().name());

        try (OutputStream out = Files.newOutputStream(profilePath)) {
            properties.store(out, "X Tweaks launcher profile");
        }
    }
}
