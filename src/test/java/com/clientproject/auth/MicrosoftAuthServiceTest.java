package com.clientproject.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

final class MicrosoftAuthServiceTest {
    @Test
    void parsesDeviceCodeAndTokenResponses() throws IOException, InterruptedException {
        HttpTransport transport = new HttpTransport() {
            private int calls;

            @Override
            public String postForm(String url, String formBody) {
                calls++;
                if (calls == 1) {
                    return "{\"verification_uri\":\"https://microsoft.com/devicelogin\",\"user_code\":\"ABCD-EFGH\",\"device_code\":\"DEV123\",\"interval\":5}";
                }
                return "{\"access_token\":\"AT\",\"refresh_token\":\"RT\",\"token_type\":\"Bearer\"}";
            }
        };

        MicrosoftAuthService service = new MicrosoftAuthService(transport, "client-id");
        MicrosoftDeviceCodeStart start = service.startDeviceLogin();
        MicrosoftToken token = service.pollForToken(start.deviceCode());

        assertEquals("https://microsoft.com/devicelogin", start.verificationUri());
        assertEquals("ABCD-EFGH", start.userCode());
        assertEquals("AT", token.accessToken());
        assertEquals("RT", token.refreshToken());
        assertEquals("Bearer", token.tokenType());
    }
}
