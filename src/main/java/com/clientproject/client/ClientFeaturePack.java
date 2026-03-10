package com.clientproject.client;

import com.clientproject.client.chat.AutoMessageModule;
import com.clientproject.client.chat.ChatCustomizationModule;
import com.clientproject.client.gameplay.GameplayModules;
import com.clientproject.client.modules.BaseModule;
import com.clientproject.client.modules.ModuleCategory;
import com.clientproject.client.modules.ModuleRegistry;
import com.clientproject.client.performance.PerformanceProfile;
import com.clientproject.client.visual.OptiFineUltraModule;
import com.clientproject.client.visual.VisualModules;
import java.util.List;

public final class ClientFeaturePack {
    private final ModuleRegistry registry = new ModuleRegistry();
    private final PerformanceProfile performanceProfile = PerformanceProfile.defaultProfile();
    private final ClientTargetProfile targetProfile = ClientTargetProfile.pvp189();

    public ClientFeaturePack() {
        registerVisualModules();
        registerHudModules();
        registerGameplayModules();
        registerChatModules();
        registerPerformanceModules();
    }

    private void registerVisualModules() {
        registry.register(new OptiFineUltraModule());
        registry.register(VisualModules.betterSky());
        registry.register(VisualModules.particlesMod());
        registry.register(VisualModules.fullbright());
        registry.register(VisualModules.clearGlassWater());
        registry.register(VisualModules.noHurtCam());
    }

    private void registerHudModules() {
        registry.register(hudModule("keystrokes", "Keystrokes (KeyMod)"));
        registry.register(hudModule("cps_counter", "CPS Counter"));
        registry.register(hudModule("armor_hud", "Armor HUD"));
        registry.register(hudModule("status_effect_hud", "Status Effect HUD"));
        registry.register(hudModule("fps_ping_display", "FPS & Ping (MS) Display"));
        registry.register(hudModule("reach_display", "Reach Display"));
        registry.register(hudModule("combo_counter", "Combo Counter"));
        registry.register(hudModule("tnt_timer", "TNT Timer"));
    }

    private void registerGameplayModules() {
        registry.register(GameplayModules.toggleSprint());
        registry.register(GameplayModules.toggleSneak());
        registry.register(GameplayModules.backView());
    }

    private void registerChatModules() {
        registry.register(new AutoMessageModule());
        registry.register(new ChatCustomizationModule());
    }

    private void registerPerformanceModules() {
        registry.register(simple(ModuleCategory.PERFORMANCE, "input_lag_reduction", "Input Lag Reduction"));
        registry.register(simple(ModuleCategory.PERFORMANCE, "entity_tile_culling", "Entity & Tile Culling"));
        registry.register(simple(ModuleCategory.PERFORMANCE, "lazy_data_loading", "Lazy Data Loading"));
        registry.register(simple(ModuleCategory.PERFORMANCE, "smart_animations", "Smart Animations"));
    }

    private BaseModule hudModule(String id, String name) {
        return simple(ModuleCategory.HUD, id, name);
    }

    private BaseModule simple(ModuleCategory category, String id, String displayName) {
        return new BaseModule(id, displayName, category, true) {};
    }

    public ModuleRegistry registry() {
        return registry;
    }

    public PerformanceProfile performanceProfile() {
        return performanceProfile;
    }

    public List<String> enabledModuleNames() {
        return registry.all().stream().filter(module -> module.enabled()).map(module -> module.displayName()).toList();
    }

    public ClientTargetProfile targetProfile() {
        return targetProfile;
    }
}
