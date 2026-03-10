package com.clientproject.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

final class OfflineAuthServiceTest {
    @Test
    void createsLocalOnlySession() {
        SessionProfile session = new OfflineAuthService().createLocalSession("LocalPlayer");

        assertEquals(AuthMode.OFFLINE_LOCAL, session.authMode());
        assertFalse(session.multiplayerAllowed());
    }
}
