package com.clientproject.security;

import com.clientproject.auth.AuthMode;
import com.clientproject.auth.SessionProfile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SessionPersistenceService {
    private static final String SESSION_FILE = "session.properties";
    private static final String TOKEN_FILE = "session.token";

    private final SecureTokenStore tokenStore = new SecureTokenStore();

    public void save(Path launcherRoot, SessionProfile session) throws IOException {
        Files.createDirectories(launcherRoot);
        Path props = launcherRoot.resolve(SESSION_FILE);
        String content = "authMode=" + session.authMode().name() + "\n"
                + "username=" + session.username() + "\n"
                + "multiplayerAllowed=" + session.multiplayerAllowed() + "\n"
                + "playerId=" + session.playerId() + "\n";
        Files.writeString(props, content, StandardCharsets.UTF_8);
        tokenStore.save(launcherRoot.resolve(TOKEN_FILE), session.accessToken());
    }

    public SessionProfile load(Path launcherRoot) throws IOException {
        Path props = launcherRoot.resolve(SESSION_FILE);
        if (!Files.exists(props)) {
            return new SessionProfile(AuthMode.OFFLINE_LOCAL, "Player", "", false, "");
        }
        String[] lines = Files.readString(props, StandardCharsets.UTF_8).split("\\R");
        String authMode = value(lines, "authMode", AuthMode.OFFLINE_LOCAL.name());
        String username = value(lines, "username", "Player");
        boolean multiplayerAllowed = Boolean.parseBoolean(value(lines, "multiplayerAllowed", "false"));
        String playerId = value(lines, "playerId", "");
        String token = tokenStore.load(launcherRoot.resolve(TOKEN_FILE));
        return new SessionProfile(AuthMode.valueOf(authMode), username, token, multiplayerAllowed, playerId);
    }

    public void clear(Path launcherRoot) throws IOException {
        Files.deleteIfExists(launcherRoot.resolve(SESSION_FILE));
        tokenStore.clear(launcherRoot.resolve(TOKEN_FILE));
    }

    private static String value(String[] lines, String key, String fallback) {
        for (String line : lines) {
            if (line.startsWith(key + "=")) {
                return line.substring(key.length() + 1);
            }
        }
        return fallback;
    }
}
