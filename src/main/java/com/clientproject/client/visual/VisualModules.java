package com.clientproject.client.visual;

import com.clientproject.client.modules.BaseModule;
import com.clientproject.client.modules.ModuleCategory;

public final class VisualModules {
    private VisualModules() {}

    public static BaseModule betterSky() {
        return new SimpleVisualModule("better_sky", "BetterSky");
    }

    public static BaseModule particlesMod() {
        return new SimpleVisualModule("particles_mod", "Particles Mod");
    }

    public static BaseModule fullbright() {
        return new SimpleVisualModule("fullbright", "Fullbright");
    }

    public static BaseModule clearGlassWater() {
        return new SimpleVisualModule("clear_glass_water", "Clear Glass/Water");
    }

    public static BaseModule noHurtCam() {
        return new SimpleVisualModule("no_hurt_cam", "NoHurtCam");
    }

    private static final class SimpleVisualModule extends BaseModule {
        private int intensity = 0;

        private SimpleVisualModule(String id, String displayName) {
            super(id, displayName, ModuleCategory.VISUAL, true);
        }

        public int intensity() {
            return intensity;
        }

        public void setIntensity(int intensity) {
            if (intensity < 0 || intensity > 100) {
                throw new IllegalArgumentException("intensity out of range");
            }
            this.intensity = intensity;
        }
    }
}
