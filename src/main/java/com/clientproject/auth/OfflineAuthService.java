package com.clientproject.auth;

public final class OfflineAuthService {
    public SessionProfile createLocalSession(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }
        return SessionProfile.offline(username);
    }
}
