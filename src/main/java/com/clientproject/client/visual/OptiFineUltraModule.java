package com.clientproject.client.visual;

import com.clientproject.client.modules.BaseModule;
import com.clientproject.client.modules.ModuleCategory;

public final class OptiFineUltraModule extends BaseModule {
    private static final int MIN_RENDER_DISTANCE = 2;
    private static final int MAX_RENDER_DISTANCE = 64;

    private boolean shaders = false;
    private boolean zoom = true;
    private int renderDistanceChunks = 8;

    public OptiFineUltraModule() {
        super("optifine_ultra", "OptiFine Ultra", ModuleCategory.VISUAL, true);
    }

    public boolean shaders() {
        return shaders;
    }

    public void setShaders(boolean shaders) {
        this.shaders = shaders;
    }

    public boolean zoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    public int renderDistanceChunks() {
        return renderDistanceChunks;
    }

    public void setRenderDistanceChunks(int renderDistanceChunks) {
        if (renderDistanceChunks < MIN_RENDER_DISTANCE || renderDistanceChunks > MAX_RENDER_DISTANCE) {
            throw new IllegalArgumentException("renderDistanceChunks out of range");
        }
        this.renderDistanceChunks = renderDistanceChunks;
    }
}
