package com.clientproject.auth;

public record SessionProfile(
        AuthMode authMode,
        String username,
        String accessToken,
        boolean multiplayerAllowed,
        String playerId
) {
    public static SessionProfile offline(String username) {
        return new SessionProfile(AuthMode.OFFLINE_LOCAL, username, "OFFLINE", false, "");
    }
}
