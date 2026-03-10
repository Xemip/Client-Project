package com.clientproject.auth;

import java.io.IOException;

/**
 * Thin HTTP abstraction used by auth and update flows.
 */
public interface HttpTransport {
    String postForm(String url, String formBody) throws IOException, InterruptedException;

    default String postJson(String url, String jsonBody, String bearerToken) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("postJson not implemented");
    }

    default String getJson(String url, String bearerToken) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("getJson not implemented");
    }
}
