package com.clientproject.launcher.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.clientproject.auth.AuthMode;
import com.clientproject.launcher.LauncherConfigStore;
import com.clientproject.launcher.LauncherProfile;
import com.clientproject.launcher.LauncherSettingsService;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class LauncherSettingsServiceTest {
    @Test
    void updatesRuntimeAndSessionSettings() throws Exception {
        Path root = Files.createTempDirectory("xt-gui-settings");
        LauncherSettingsService service = new LauncherSettingsService(new LauncherConfigStore(), root);
        LauncherProfile profile = service.load();

        LauncherProfile runtime = service.updateRuntimeSettings(
                profile,
                "java",
                1024,
                3072,
                root.resolve("game"),
                root.resolve("assets"),
                root.resolve("libs"));
        assertEquals(3072, runtime.maxMemoryMb());

        LauncherProfile session = service.updateSession(runtime, "GuiUser", "TOKEN", AuthMode.MICROSOFT);
        assertEquals("GuiUser", session.username());
        assertEquals(AuthMode.MICROSOFT, session.authMode());
    }
}
