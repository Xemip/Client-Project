package com.clientproject.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.clientproject.auth.AuthMode;
import com.clientproject.auth.SessionProfile;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class SessionPersistenceServiceTest {
    @Test
    void savesAndLoadsSessionWithTokenVault() throws Exception {
        Path root = Files.createTempDirectory("xt-session");
        SessionPersistenceService service = new SessionPersistenceService();
        SessionProfile input = new SessionProfile(AuthMode.MICROSOFT, "Player", "TOKEN123", true, "uuid");

        service.save(root, input);
        SessionProfile loaded = service.load(root);

        assertEquals(input.authMode(), loaded.authMode());
        assertEquals(input.username(), loaded.username());
        assertEquals(input.accessToken(), loaded.accessToken());
        assertEquals(input.multiplayerAllowed(), loaded.multiplayerAllowed());
        assertEquals(input.playerId(), loaded.playerId());

        service.clear(root);
        SessionProfile fallback = service.load(root);
        assertEquals(AuthMode.OFFLINE_LOCAL, fallback.authMode());
    }
}
