package com.clientproject.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

final class FrameTimeSmootherTest {
    @Test
    void computesAverageWithWindowEviction() {
        FrameTimeSmoother smoother = new FrameTimeSmoother(3);

        smoother.recordFrame(10);
        smoother.recordFrame(20);
        smoother.recordFrame(30);
        assertEquals(20, smoother.averageNanos());

        smoother.recordFrame(40);
        assertEquals(30, smoother.averageNanos());
    }
}
