package com.clientproject.launcher;

public class ClientLauncher {

    public LaunchPlan buildLaunchPlan(ClientLaunchConfig config, LauncherProfile profile) {
        return new LaunchPlan(config.getGameDir(), profile);
    }

    public void launch(LaunchPlan plan) {
        System.out.println("Launching client...");
        System.out.println(plan);
    }
}
