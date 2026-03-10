package com.clientproject.multiplayer;

import com.clientproject.auth.AuthMode;
import com.clientproject.auth.SessionProfile;

public final class MultiplayerAccessPolicy {
    public boolean canJoinOnline(SessionProfile session) {
        return session != null
                && session.authMode() == AuthMode.MICROSOFT
                && session.multiplayerAllowed()
                && session.accessToken() != null
                && !session.accessToken().isBlank();
    }

    public String denyReason(SessionProfile session) {
        if (session == null) {
            return "No active session.";
        }
        if (session.authMode() != AuthMode.MICROSOFT) {
            return "Online multiplayer requires a Microsoft-authenticated session.";
        }
        if (!session.multiplayerAllowed()) {
            return "Session policy disallows online multiplayer.";
        }
        if (session.accessToken() == null || session.accessToken().isBlank()) {
            return "Missing access token.";
        }
        return "";
    }
}
