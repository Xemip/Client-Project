package com.clientproject.optimization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clientproject.client.performance.PerformanceProfile;
import com.clientproject.client.visual.OptiFineUltraModule;
import com.clientproject.launcher.LauncherProfile;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class OptimizationPresetTest {
    @Test
    void pvpDefaultsArePerformanceOriented() {
        OptimizationPreset pvp = OptimizationPreset.competitivePvp();
        PerformanceProfile profile = PerformanceProfile.defaultProfile();
        OptiFineUltraModule optifine = new OptiFineUltraModule();
        LauncherProfile launcher = LauncherProfile.defaultProfile(Path.of("/tmp/x"));

        assertEquals(240, pvp.maxFps());
        assertTrue(pvp.reducedInputLatency());
        assertEquals(240, profile.preferredMaxFps());
        assertEquals(8, optifine.renderDistanceChunks());
        assertTrue(launcher.maxMemoryMb() <= 3072);
    }
}
