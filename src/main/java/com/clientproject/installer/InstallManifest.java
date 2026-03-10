package com.clientproject.installer;

import java.time.Instant;
import java.util.List;

public record InstallManifest(
        String clientName,
        String minecraftVersion,
        String launcherMainClass,
        String installerVersion,
        Instant builtAtUtc,
        List<String> includedModules,
        List<String> bundleEntries
) {
}
