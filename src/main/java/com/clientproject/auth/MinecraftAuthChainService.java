package com.clientproject.auth;

import java.io.IOException;

/**
 * Production auth chain: Microsoft -> Xbox Live -> XSTS -> Minecraft -> Profile.
 */
public final class MinecraftAuthChainService {
    private static final String XBL_AUTH_URL = "https://user.auth.xboxlive.com/user/authenticate";
    private static final String XSTS_AUTH_URL = "https://xsts.auth.xboxlive.com/xsts/authorize";
    private static final String MC_LOGIN_URL = "https://api.minecraftservices.com/authentication/login_with_xbox";
    private static final String MC_PROFILE_URL = "https://api.minecraftservices.com/minecraft/profile";

    private final HttpTransport transport;

    public MinecraftAuthChainService(HttpTransport transport) {
        this.transport = transport;
    }

    public XboxLiveToken authenticateXboxLive(String microsoftAccessToken) throws IOException, InterruptedException {
        String body = "{" +
                "\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"d=" + esc(microsoftAccessToken) + "\"}," +
                "\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}";
        String response = transport.postJson(XBL_AUTH_URL, body, null);
        String token = MicrosoftAuthService.extract(response, "Token");
        String uhs = extractUserHash(response);
        return new XboxLiveToken(token, uhs);
    }

    public XstsToken authorizeXsts(XboxLiveToken xboxLiveToken) throws IOException, InterruptedException {
        String body = "{" +
                "\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"" + esc(xboxLiveToken.token()) + "\"]}," +
                "\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}";
        String response = transport.postJson(XSTS_AUTH_URL, body, null);
        String token = MicrosoftAuthService.extract(response, "Token");
        String uhs = extractUserHash(response);
        return new XstsToken(token, uhs);
    }

    public MinecraftAccessToken loginToMinecraft(XstsToken xstsToken) throws IOException, InterruptedException {
        String identityToken = "XBL3.0 x=" + xstsToken.userHash() + ";" + xstsToken.token();
        String body = "{\"identityToken\":\"" + esc(identityToken) + "\"}";
        String response = transport.postJson(MC_LOGIN_URL, body, null);
        return new MinecraftAccessToken(MicrosoftAuthService.extract(response, "access_token"));
    }

    public MinecraftProfile fetchProfile(MinecraftAccessToken accessToken) throws IOException, InterruptedException {
        String response = transport.getJson(MC_PROFILE_URL, accessToken.accessToken());
        return new MinecraftProfile(
                MicrosoftAuthService.extract(response, "id"),
                MicrosoftAuthService.extract(response, "name")
        );
    }

    private static String extractUserHash(String json) {
        int i = json.indexOf("\"uhs\":\"");
        if (i < 0) {
            return "";
        }
        int start = i + "\"uhs\":\"".length();
        int end = json.indexOf('"', start);
        return end < 0 ? "" : json.substring(start, end);
    }

    private static String esc(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
