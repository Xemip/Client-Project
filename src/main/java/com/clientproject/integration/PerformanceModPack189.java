package com.clientproject.integration;

import java.net.URI;
import java.util.List;

/**
 * Curated metadata for 1.8.9 PvP/performance integrations.
 *
 * Note: OptiFine is marked non-redistributable and should be user-provided.
 */
public final class PerformanceModPack189 {
    private PerformanceModPack189() {
    }

    public static List<ModDescriptor> recommendedMods() {
        return List.of(
                new ModDescriptor("optifine_1_8_9", "OptiFine 1.8.9", "1.8.9", URI.create("https://optifine.net/downloads"), false),
                new ModDescriptor("patcher_1_8_9", "Patcher 1.8.9", "1.8.9", URI.create("https://sk1er.club/mods/patcher"), true),
                new ModDescriptor("foamfix_1_8_9", "FoamFix (legacy)", "1.8.9", URI.create("https://www.curseforge.com/minecraft/mc-mods/foamfix-for-minecraft"), true)
        );
    }
}
