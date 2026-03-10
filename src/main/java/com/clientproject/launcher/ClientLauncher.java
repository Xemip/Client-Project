package com.clientproject.launcher;

public class ClientLauncher {

    public LaunchPlan buildLaunchPlan(ClientLaunchConfig config, LauncherProfile profile) {
        return new LaunchPlan(config, profile);
    }

}
