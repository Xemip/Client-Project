package com.clientproject.engine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

final class ChunkVisibilityGridTest {
    @Test
    void tracksVisibilityInFixedRadius() {
        ChunkVisibilityGrid grid = new ChunkVisibilityGrid(2);

        assertFalse(grid.isVisible(0, 0));
        grid.markVisible(0, 0);
        grid.markVisible(1, -1);

        assertTrue(grid.isVisible(0, 0));
        assertTrue(grid.isVisible(1, -1));

        grid.clearVisible(0, 0);
        assertFalse(grid.isVisible(0, 0));

        grid.clearAll();
        assertFalse(grid.isVisible(1, -1));
    }
}
