package com.clientproject.benchmark;

import com.clientproject.engine.FastEventBus;
import com.clientproject.engine.FrameTimeSmoother;
import com.clientproject.launcher.LauncherProfile;
import com.clientproject.launcher.RuntimePathsResolver;
import java.nio.file.Path;

public final class PerformanceBenchmarkMain {
    private PerformanceBenchmarkMain() {
    }

    public static void main(String[] args) throws Exception {
        benchmarkEventBus();
        benchmarkFrameSmoother();
        benchmarkPathResolver();
    }

    private static void benchmarkEventBus() {
        FastEventBus bus = new FastEventBus();
        final int listeners = 32;
        final int events = 100_000;
        for (int i = 0; i < listeners; i++) {
            bus.subscribe(Integer.class, ignored -> {});
        }
        long start = System.nanoTime();
        for (int i = 0; i < events; i++) {
            bus.publish(i);
        }
        long elapsed = System.nanoTime() - start;
        System.out.println("benchmark.eventbus.ns=" + elapsed + " events=" + events + " listeners=" + listeners);
    }

    private static void benchmarkFrameSmoother() {
        FrameTimeSmoother smoother = new FrameTimeSmoother(120);
        long start = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++) {
            smoother.recordFrame(16_000_000 + (i % 1_000));
            smoother.averageNanos();
        }
        long elapsed = System.nanoTime() - start;
        System.out.println("benchmark.framesmoother.ns=" + elapsed);
    }

    private static void benchmarkPathResolver() {
        RuntimePathsResolver resolver = new RuntimePathsResolver();
        LauncherProfile profile = LauncherProfile.defaultProfile(Path.of(System.getProperty("user.home"), ".x-tweaks"));
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            resolver.resolveVersionJar(profile.gameDirectory(), "1.8.9");
        }
        long elapsed = System.nanoTime() - start;
        System.out.println("benchmark.pathresolver.ns=" + elapsed);
    }
}
