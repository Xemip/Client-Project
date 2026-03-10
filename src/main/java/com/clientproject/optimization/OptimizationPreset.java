package com.clientproject.optimization;

public record OptimizationPreset(
        String name,
        int maxFps,
        int chunkUpdateBudget,
        boolean smartAnimations,
        boolean entityCulling,
        boolean tileCulling,
        boolean reducedInputLatency
) {
    public static OptimizationPreset competitivePvp() {
        return new OptimizationPreset("Competitive PvP", 240, 3, true, true, true, true);
    }

    public static OptimizationPreset qualityBalanced() {
        return new OptimizationPreset("Quality Balanced", 144, 6, true, true, false, true);
    }
}
