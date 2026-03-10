package com.clientproject.launcher;

public class ClientLauncher {

    public void launch(ClientLaunchConfig config) {
        System.out.println("Launching client version " + config.getVersion());
        System.out.println("Game directory: " + config.getGameDir());
    }
}
