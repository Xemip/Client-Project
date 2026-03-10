package com.clientproject.auth;

public record MicrosoftDeviceCodeStart(String verificationUri, String userCode, String deviceCode, int intervalSeconds) {
}
