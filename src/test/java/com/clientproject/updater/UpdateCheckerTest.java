package com.clientproject.updater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clientproject.auth.HttpTransport;
import org.junit.jupiter.api.Test;

final class UpdateCheckerTest {
    @Test
    void parsesManifestAndDetectsNewVersion() throws Exception {
        HttpTransport transport = new HttpTransport() {
            @Override
            public String postForm(String url, String formBody) {
                return "";
            }

            @Override
            public String getJson(String url, String bearerToken) {
                return "{\"latestVersion\":\"0.9.0\",\"downloadUrl\":\"https://example.com/xt.zip\",\"notes\":\"perf\"}";
            }
        };

        UpdateChecker checker = new UpdateChecker(transport);
        UpdateCheckResult res = checker.check("0.8.0", "https://example.com/manifest.json");

        assertTrue(res.updateAvailable());
        assertEquals("0.9.0", res.updateInfo().latestVersion());
        assertEquals("https://example.com/xt.zip", res.updateInfo().downloadUrl());

        assertFalse(UpdateChecker.compare("0.8.0", "0.8.1") > 0);
        assertEquals(0, UpdateChecker.compare("1.0.0", "1.0.0"));
    }
}
