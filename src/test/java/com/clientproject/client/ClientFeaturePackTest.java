package com.clientproject.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clientproject.client.modules.ModuleCategory;
import org.junit.jupiter.api.Test;

final class ClientFeaturePackTest {
    @Test
    void registersRequestedFeatureGroups() {
        ClientFeaturePack pack = new ClientFeaturePack();

        assertTrue(pack.registry().byCategory(ModuleCategory.VISUAL).size() >= 6);
        assertTrue(pack.registry().byCategory(ModuleCategory.HUD).size() >= 8);
        assertTrue(pack.registry().byCategory(ModuleCategory.GAMEPLAY).size() >= 3);
        assertTrue(pack.registry().byCategory(ModuleCategory.CHAT).size() >= 2);
        assertTrue(pack.registry().byCategory(ModuleCategory.PERFORMANCE).size() >= 4);
        assertEquals("1.8.9", pack.targetProfile().minecraftVersion());
    }
}
