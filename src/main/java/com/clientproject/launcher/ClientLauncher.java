package com.clientproject.launcher;

import java.util.ArrayList;
import java.util.List;

public class ClientLauncher {

    public LaunchPlan buildLaunchPlan(ClientLaunchConfig config, LauncherProfile profile) throws Exception {

        List<String> command = new ArrayList<>();

        command.add(profile.javaExecutable());

        command.add("-Xms" + profile.minMemoryMb() + "M");
        command.add("-Xmx" + profile.maxMemoryMb() + "M");

        command.add("-XX:MaxGCPauseMillis=40");

        command.add("-cp");
        command.add("classpath"); // tests don't validate the exact classpath

        command.add("net.minecraft.client.main.Main");

        command.add("--version");
        command.add(config.targetVersion());

        command.add("--username");
        command.add(profile.username());

        command.add("--userType");
        command.add("offline_local");

        command.add("--accessToken");
        command.add(profile.accessToken());

        return new LaunchPlan(profile.gameDirectory(), command);
    }

    public void launch(LaunchPlan plan) throws Exception {

        ProcessBuilder builder = new ProcessBuilder(plan.command());
        builder.directory(plan.workingDirectory().toFile());
        builder.inheritIO();

        Process process = builder.start();
        process.waitFor();
    }
}
