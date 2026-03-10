package com.clientproject.engine;

import com.clientproject.client.ClientFeaturePack;

/**
 * First bootstrap for an optimization-focused client architecture.
 */
public final class ClientBootstrap {
    private final FastEventBus eventBus = new FastEventBus();
    private final FrameTimeSmoother frameTimeSmoother = new FrameTimeSmoother(120);
    private final ChunkVisibilityGrid visibilityGrid = new ChunkVisibilityGrid(16);
    private final ClientFeaturePack featurePack = new ClientFeaturePack();

    public FastEventBus eventBus() {
        return eventBus;
    }

    public FrameTimeSmoother frameTimeSmoother() {
        return frameTimeSmoother;
    }

    public ChunkVisibilityGrid visibilityGrid() {
        return visibilityGrid;
    }

    public ClientFeaturePack featurePack() {
        return featurePack;
    }
}
