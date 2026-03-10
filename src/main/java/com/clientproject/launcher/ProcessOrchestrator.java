package com.clientproject.launcher;

import java.io.IOException;

public final class ProcessOrchestrator {
    public Process start(LaunchPlan plan) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(plan.command());
        builder.directory(plan.workingDirectory().toFile());
        builder.inheritIO();
        return builder.start();
    }
}
