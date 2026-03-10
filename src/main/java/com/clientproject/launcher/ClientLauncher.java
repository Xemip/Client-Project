package com.clientproject.launcher;

import java.util.ArrayList;
import java.util.List;

public final class ClientLauncher {
    public List<String> buildCommand(ClientLaunchConfig config) {
        List<String> command = new ArrayList<>();
        command.add(config.javaExecutable());
        command.addAll(config.jvmArgs());
        command.add("-jar");
        command.add("client-1.8.9.jar");
        command.addAll(config.gameArgs());
        return command;
    }
}
