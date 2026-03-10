package com.clientproject.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

final class AuthFacadeChainTest {
    @Test
    void returnsMinecraftSessionProfileAfterMicrosoftChain() throws IOException, InterruptedException {
        HttpTransport transport = new HttpTransport() {
            private int formCalls;

            @Override
            public String postForm(String url, String formBody) {
                formCalls++;
                if (formCalls == 1) {
                    return "{\"verification_uri\":\"https://microsoft.com/devicelogin\",\"user_code\":\"CODE\",\"device_code\":\"DEV\",\"interval\":5}";
                }
                return "{\"access_token\":\"MS_ACCESS\",\"refresh_token\":\"RT\",\"token_type\":\"Bearer\"}";
            }

            @Override
            public String postJson(String url, String jsonBody, String bearerToken) {
                if (url.contains("user.auth.xboxlive.com")) {
                    return "{\"Token\":\"XBL_TOKEN\",\"DisplayClaims\":{\"xui\":[{\"uhs\":\"UHS\"}]}}";
                }
                if (url.contains("xsts.auth.xboxlive.com")) {
                    return "{\"Token\":\"XSTS_TOKEN\",\"DisplayClaims\":{\"xui\":[{\"uhs\":\"UHS\"}]}}";
                }
                return "{\"access_token\":\"MC_TOKEN\"}";
            }

            @Override
            public String getJson(String url, String bearerToken) {
                return "{\"id\":\"uuid-1\",\"name\":\"RealName\"}";
            }
        };

        AuthFacade facade = new AuthFacade(
                new MicrosoftAuthService(transport, "client-id"),
                new OfflineAuthService(),
                new MinecraftAuthChainService(transport));

        SessionProfile session = facade.completeMicrosoftLogin("", "DEV");
        assertEquals(AuthMode.MICROSOFT, session.authMode());
        assertEquals("RealName", session.username());
        assertEquals("MC_TOKEN", session.accessToken());
        assertTrue(session.multiplayerAllowed());
        assertEquals("uuid-1", session.playerId());
    }
}
