package com.clientproject.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

final class ClientLauncherTest {
    @Test
    void buildsJavaCommandFor189Client() {
        ClientLaunchConfig config = new ClientLaunchConfig(
                "1.8.9",
                Path.of("game"),
                Path.of("assets"),
                "java",
                List.of("-Xmx2G"),
                List.of("--version", "1.8.9")
        );

        List<String> command = new ClientLauncher().buildCommand(config);

        assertEquals("java", command.get(0));
        assertTrue(command.contains("client-1.8.9.jar"));
        assertTrue(command.contains("--version"));
    }
}
