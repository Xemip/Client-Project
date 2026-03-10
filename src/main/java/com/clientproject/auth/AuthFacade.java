package com.clientproject.auth;

import java.io.IOException;

public final class AuthFacade {
    private final MicrosoftAuthService microsoftAuthService;
    private final OfflineAuthService offlineAuthService;
    private final MinecraftAuthChainService minecraftAuthChainService;

    public AuthFacade(
            MicrosoftAuthService microsoftAuthService,
            OfflineAuthService offlineAuthService,
            MinecraftAuthChainService minecraftAuthChainService) {
        this.microsoftAuthService = microsoftAuthService;
        this.offlineAuthService = offlineAuthService;
        this.minecraftAuthChainService = minecraftAuthChainService;
    }

    public MicrosoftDeviceCodeStart startMicrosoftLogin() throws IOException, InterruptedException {
        return microsoftAuthService.startDeviceLogin();
    }

    public SessionProfile completeMicrosoftLogin(String preferredUsername, String deviceCode) throws IOException, InterruptedException {
        MicrosoftToken microsoftToken = microsoftAuthService.pollForToken(deviceCode);
        XboxLiveToken xboxLiveToken = minecraftAuthChainService.authenticateXboxLive(microsoftToken.accessToken());
        XstsToken xstsToken = minecraftAuthChainService.authorizeXsts(xboxLiveToken);
        MinecraftAccessToken minecraftAccessToken = minecraftAuthChainService.loginToMinecraft(xstsToken);
        MinecraftProfile profile = minecraftAuthChainService.fetchProfile(minecraftAccessToken);

        String username = preferredUsername == null || preferredUsername.isBlank() ? profile.name() : preferredUsername;
        return new SessionProfile(AuthMode.MICROSOFT, username, minecraftAccessToken.accessToken(), true, profile.id());
    }

    public SessionProfile startOfflineLocal(String username) {
        return offlineAuthService.createLocalSession(username);
    }
}
