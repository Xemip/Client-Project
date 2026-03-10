package com.clientproject.updater;

public record UpdateCheckResult(boolean updateAvailable, String currentVersion, UpdateInfo updateInfo) {
}
