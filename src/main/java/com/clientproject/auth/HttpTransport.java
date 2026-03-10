package com.clientproject.auth;

import java.io.IOException;

public interface HttpTransport {
    String postForm(String url, String formBody) throws IOException, InterruptedException;
}
