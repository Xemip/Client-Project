package com.clientproject.launcher;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ClientLauncher {
    private static final String MC_MAIN_CLASS = "net.minecraft.client.main.Main";

    public LaunchPlan buildLaunchPlan(ClientLaunchConfig config, LauncherProfile profile) throws IOException {
        RuntimePathsResolver resolver = new RuntimePathsResolver();
        Path versionJar = resolver.resolveVersionJar(config.gameDirectory(), config.minecraftVersion());
        List<Path> libraries = resolver.resolveLibraries(profile.librariesDirectory());

        List<String> command = new ArrayList<>();
        command.add(config.javaExecutable());
        command.addAll(config.jvmArgs());
        command.add("-cp");
        command.add(toClasspath(versionJar, libraries));
        command.add(MC_MAIN_CLASS);
        command.addAll(config.gameArgs());

        return new LaunchPlan(config.gameDirectory(), command);
    }

    public Process launch(LaunchPlan plan) throws IOException {
        return new ProcessOrchestrator().start(plan);
    }

    private String toClasspath(Path versionJar, List<Path> libraries) {
        String separator = System.getProperty("path.separator");
        StringBuilder classpath = new StringBuilder(versionJar.toString());
        for (Path library : libraries) {
            classpath.append(separator).append(library);
        }
        return classpath.toString();
    }
}
