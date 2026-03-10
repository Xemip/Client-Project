package com.clientproject.client.performance;

public record PerformanceProfile(
        boolean inputLagReduction,
        boolean entityCulling,
        boolean tileCulling,
        boolean lazyDataLoading,
        boolean smartAnimations
) {
    public static PerformanceProfile defaultProfile() {
        return new PerformanceProfile(true, true, true, true, true);
    }
}
