package com.clientproject.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

final class MinecraftAuthChainServiceTest {
    @Test
    void performsMicrosoftToMinecraftChainParsing() throws IOException, InterruptedException {
        HttpTransport transport = new HttpTransport() {
            @Override
            public String postForm(String url, String formBody) {
                return "";
            }

            @Override
            public String postJson(String url, String jsonBody, String bearerToken) {
                if (url.contains("user.auth.xboxlive.com")) {
                    return "{\"Token\":\"XBL_TOKEN\",\"DisplayClaims\":{\"xui\":[{\"uhs\":\"UHS123\"}]}}";
                }
                if (url.contains("xsts.auth.xboxlive.com")) {
                    return "{\"Token\":\"XSTS_TOKEN\",\"DisplayClaims\":{\"xui\":[{\"uhs\":\"UHS123\"}]}}";
                }
                return "{\"access_token\":\"MC_ACCESS\"}";
            }

            @Override
            public String getJson(String url, String bearerToken) {
                return "{\"id\":\"player-id\",\"name\":\"PlayerName\"}";
            }
        };

        MinecraftAuthChainService service = new MinecraftAuthChainService(transport);
        XboxLiveToken xbl = service.authenticateXboxLive("MS");
        XstsToken xsts = service.authorizeXsts(xbl);
        MinecraftAccessToken mc = service.loginToMinecraft(xsts);
        MinecraftProfile profile = service.fetchProfile(mc);

        assertEquals("XBL_TOKEN", xbl.token());
        assertEquals("XSTS_TOKEN", xsts.token());
        assertEquals("MC_ACCESS", mc.accessToken());
        assertEquals("player-id", profile.id());
        assertEquals("PlayerName", profile.name());
    }
}
