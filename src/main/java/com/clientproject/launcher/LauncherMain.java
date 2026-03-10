package com.clientproject.launcher;

import java.nio.file.Path;

public final class LauncherMain {
    private LauncherMain() {
    }

    public static void main(String[] args) {
        ClientLaunchConfig config = ClientLaunchConfig.default189(
                Path.of(System.getProperty("user.home"), ".client-project-189"),
                Path.of(System.getProperty("user.home"), ".minecraft", "assets")
        );
        ClientLauncher launcher = new ClientLauncher();
        System.out.println(String.join(" ", launcher.buildCommand(config)));
    }
}
