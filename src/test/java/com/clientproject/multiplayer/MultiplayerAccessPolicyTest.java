package com.clientproject.multiplayer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clientproject.auth.AuthMode;
import com.clientproject.auth.SessionProfile;
import org.junit.jupiter.api.Test;

final class MultiplayerAccessPolicyTest {
    @Test
    void allowsOnlineOnlyForMicrosoftSession() {
        MultiplayerAccessPolicy policy = new MultiplayerAccessPolicy();

        SessionProfile microsoft = new SessionProfile(AuthMode.MICROSOFT, "Player", "TOKEN", true, "id");
        SessionProfile offline = new SessionProfile(AuthMode.OFFLINE_LOCAL, "Local", "OFFLINE", false, "");

        assertTrue(policy.canJoinOnline(microsoft));
        assertFalse(policy.canJoinOnline(offline));
    }
}
