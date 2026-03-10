package com.clientproject.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class SecureTokenStoreTest {
    @Test
    void roundTripTokenStorageAndClear() throws Exception {
        Path dir = Files.createTempDirectory("xt-token");
        Path tokenFile = dir.resolve("token.dat");
        SecureTokenStore store = new SecureTokenStore();

        store.save(tokenFile, "SECRET_TOKEN");
        assertTrue(Files.exists(tokenFile));
        assertEquals("SECRET_TOKEN", store.load(tokenFile));

        store.clear(tokenFile);
        assertEquals("", store.load(tokenFile));
    }
}
