package com.clientproject.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * Lightweight obfuscated token vault for local launcher storage.
 * Not a replacement for hardware-backed secure storage.
 */
public final class SecureTokenStore {
    public void save(Path path, String token) throws IOException {
        Files.createDirectories(path.getParent());
        byte[] payload = xor(token.getBytes(StandardCharsets.UTF_8));
        Files.writeString(path, Base64.getEncoder().encodeToString(payload), StandardCharsets.UTF_8);
    }

    public String load(Path path) throws IOException {
        if (!Files.exists(path)) {
            return "";
        }
        byte[] encoded = Base64.getDecoder().decode(Files.readString(path, StandardCharsets.UTF_8));
        return new String(xor(encoded), StandardCharsets.UTF_8);
    }

    public void clear(Path path) throws IOException {
        Files.deleteIfExists(path);
    }

    private byte[] xor(byte[] input) {
        byte[] key = key();
        byte[] out = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = (byte) (input[i] ^ key[i % key.length]);
        }
        return out;
    }

    private byte[] key() {
        String seed = System.getProperty("user.name", "user") + "::" + System.getProperty("os.name", "os") + "::xtweaks";
        return seed.getBytes(StandardCharsets.UTF_8);
    }
}
