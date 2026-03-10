package com.clientproject.engine;

/**
 * Stores recent frame times in a ring buffer and returns a moving average.
 * Helps stabilize pacing logic and adaptive graphics decisions.
 */
public final class FrameTimeSmoother {
    private final long[] frameTimesNanos;
    private int writeIndex;
    private int size;
    private long sum;

    public FrameTimeSmoother(int windowSize) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("windowSize must be > 0");
        }
        this.frameTimesNanos = new long[windowSize];
    }

    public void recordFrame(long frameTimeNanos) {
        if (frameTimeNanos < 0) {
            throw new IllegalArgumentException("frameTimeNanos must be >= 0");
        }

        if (size < frameTimesNanos.length) {
            size++;
        } else {
            sum -= frameTimesNanos[writeIndex];
        }

        frameTimesNanos[writeIndex] = frameTimeNanos;
        sum += frameTimeNanos;
        writeIndex = (writeIndex + 1) % frameTimesNanos.length;
    }

    public long averageNanos() {
        if (size == 0) {
            return 0;
        }
        return sum / size;
    }
}
