package com.clientproject.auth;

public record MicrosoftToken(String accessToken, String refreshToken, String tokenType) {
}
