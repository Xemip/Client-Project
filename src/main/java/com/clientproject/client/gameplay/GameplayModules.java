package com.clientproject.client.gameplay;

import com.clientproject.client.modules.BaseModule;
import com.clientproject.client.modules.ModuleCategory;

public final class GameplayModules {
    private GameplayModules() {}

    public static BaseModule toggleSprint() {
        return new GameplayToggle("toggle_sprint", "ToggleSprint");
    }

    public static BaseModule toggleSneak() {
        return new GameplayToggle("toggle_sneak", "ToggleSneak");
    }

    public static BaseModule backView() {
        return new GameplayToggle("back_view", "BackView (360° Perspective)");
    }

    private static final class GameplayToggle extends BaseModule {
        private GameplayToggle(String id, String displayName) {
            super(id, displayName, ModuleCategory.GAMEPLAY, true);
        }
    }
}
