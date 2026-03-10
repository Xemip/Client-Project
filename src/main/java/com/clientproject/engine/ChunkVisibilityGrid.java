package com.clientproject.engine;

import java.util.BitSet;

/**
 * Compact 2D visibility map for chunk columns around the player.
 *
 * This is a simple primitive that can back frustum/occlusion passes without allocating per frame.
 */
public final class ChunkVisibilityGrid {
    private final int radius;
    private final int diameter;
    private final BitSet visible;

    public ChunkVisibilityGrid(int radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("radius must be >= 0");
        }
        this.radius = radius;
        this.diameter = radius * 2 + 1;
        this.visible = new BitSet(diameter * diameter);
    }

    public void markVisible(int dx, int dz) {
        visible.set(index(dx, dz));
    }

    public void clearVisible(int dx, int dz) {
        visible.clear(index(dx, dz));
    }

    public boolean isVisible(int dx, int dz) {
        return visible.get(index(dx, dz));
    }

    public void clearAll() {
        visible.clear();
    }

    public int radius() {
        return radius;
    }

    private int index(int dx, int dz) {
        int x = dx + radius;
        int z = dz + radius;
        if (x < 0 || x >= diameter || z < 0 || z >= diameter) {
            throw new IndexOutOfBoundsException("Offset out of radius: dx=" + dx + ", dz=" + dz + ", radius=" + radius);
        }
        return z * diameter + x;
    }
}
