package com.clientproject.launcher;

import java.util.ArrayList;
import java.util.List;

public class ClientLauncher {

    public LaunchPlan buildLaunchPlan(ClientLaunchConfig config, LauncherProfile profile) {

        List<String> command = new ArrayList<>();

        command.add(profile.javaExecutable().toString());

        command.add("-Xms" + profile.minMemoryMb() + "M");
        command.add("-Xmx" + profile.maxMemoryMb() + "M");

        command.add("-cp");
        command.add("libraries/*;versions/" + config.targetVersion() + "/" + config.targetVersion() + ".jar");

        command.add("net.minecraft.client.main.Main");

        command.add("--username");
        command.add(profile.username());

        command.add("--accessToken");
        command.add(profile.accessToken());

        return new LaunchPlan(config.gameDirectory(), command);
    }

    public void launch(LaunchPlan plan) throws Exception {

        ProcessBuilder builder = new ProcessBuilder(plan.command());
        builder.directory(plan.workingDirectory().toFile());
        builder.inheritIO();

        Process process = builder.start();
        process.waitFor();
    }
}
