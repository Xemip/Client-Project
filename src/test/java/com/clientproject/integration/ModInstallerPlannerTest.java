package com.clientproject.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

final class ModInstallerPlannerTest {
    @Test
    void separatesRedistributableAndUserProvidedMods() {
        ModInstallPlan plan = new ModInstallerPlanner().buildPlan(PerformanceModPack189.recommendedMods());

        assertEquals(2, plan.redistributable().size());
        assertEquals(1, plan.userProvided().size());
        assertEquals("optifine_1_8_9", plan.userProvided().get(0).id());
    }
}
