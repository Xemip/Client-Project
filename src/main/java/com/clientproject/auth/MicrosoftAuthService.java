package com.clientproject.auth;

import java.io.IOException;

/**
 * Microsoft OAuth device-code authentication flow suitable for launcher sign-in.
 */
public final class MicrosoftAuthService {
    private static final String DEVICE_CODE_URL = "https://login.microsoftonline.com/consumers/oauth2/v2.0/devicecode";
    private static final String TOKEN_URL = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token";

    private final HttpTransport transport;
    private final String clientId;
    private final String scope;

    public MicrosoftAuthService(HttpTransport transport, String clientId) {
        this.transport = transport;
        this.clientId = clientId;
        this.scope = "XboxLive.signin offline_access";
    }

    public MicrosoftDeviceCodeStart startDeviceLogin() throws IOException, InterruptedException {
        String body = "client_id=" + clientId + "&scope=" + encode(scope);
        String response = transport.postForm(DEVICE_CODE_URL, body);
        return new MicrosoftDeviceCodeStart(
                extract(response, "verification_uri"),
                extract(response, "user_code"),
                extract(response, "device_code"),
                parseInt(extract(response, "interval"), 5)
        );
    }

    public MicrosoftToken pollForToken(String deviceCode) throws IOException, InterruptedException {
        String body = "grant_type=urn:ietf:params:oauth:grant-type:device_code&client_id=" + clientId
                + "&device_code=" + encode(deviceCode);
        String response = transport.postForm(TOKEN_URL, body);
        return new MicrosoftToken(
                extract(response, "access_token"),
                extract(response, "refresh_token"),
                extract(response, "token_type")
        );
    }

    private static String encode(String raw) {
        return raw.replace(" ", "%20");
    }

    private static int parseInt(String raw, int fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        return Integer.parseInt(raw);
    }

    // Small JSON extractor to avoid adding heavy parsing deps at this stage.
    static String extract(String json, String field) {
        String quotedField = "\"" + field + "\":";
        int keyIndex = json.indexOf(quotedField);
        if (keyIndex < 0) {
            return "";
        }
        int valueStart = keyIndex + quotedField.length();
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }

        if (valueStart < json.length() && json.charAt(valueStart) == '"') {
            int end = json.indexOf('"', valueStart + 1);
            return end < 0 ? "" : json.substring(valueStart + 1, end);
        }

        int end = valueStart;
        while (end < json.length() && Character.isDigit(json.charAt(end))) {
            end++;
        }
        return json.substring(valueStart, end);
    }
}
