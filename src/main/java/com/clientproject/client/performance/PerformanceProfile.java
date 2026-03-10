package com.clientproject.client.performance;

import com.clientproject.optimization.OptimizationPreset;

public record PerformanceProfile(
        boolean inputLagReduction,
        boolean entityCulling,
        boolean tileCulling,
        boolean lazyDataLoading,
        boolean smartAnimations,
        int preferredMaxFps,
        int chunkUpdateBudget
) {
    public static PerformanceProfile defaultProfile() {
        return pvpProfile();
    }

    public static PerformanceProfile pvpProfile() {
        return fromPreset(OptimizationPreset.competitivePvp());
    }

    public static PerformanceProfile fromPreset(OptimizationPreset preset) {
        return new PerformanceProfile(
                preset.reducedInputLatency(),
                preset.entityCulling(),
                preset.tileCulling(),
                true,
                preset.smartAnimations(),
                preset.maxFps(),
                preset.chunkUpdateBudget()
        );
    }
}
