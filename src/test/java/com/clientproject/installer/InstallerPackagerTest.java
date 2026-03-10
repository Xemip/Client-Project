package com.clientproject.installer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipFile;
import org.junit.jupiter.api.Test;

final class InstallerPackagerTest {
    @Test
    void createsInstallerZip() throws Exception {
        Path zip = Path.of("target", "test-installer.zip");
        Files.createDirectories(zip.getParent());

        new InstallerPackager().createInstallerBundle(zip);

        assertTrue(Files.exists(zip));
        assertTrue(Files.size(zip) > 0);

        try (ZipFile zf = new ZipFile(zip.toFile())) {
            assertTrue(zf.getEntry("X-Tweaks/install.sh") != null);
            assertTrue(zf.getEntry("X-Tweaks/install.bat") != null);
            assertTrue(zf.getEntry("X-Tweaks/uninstall.sh") != null);
            assertTrue(zf.getEntry("X-Tweaks/uninstall.bat") != null);
            assertTrue(zf.getEntry("X-Tweaks/bin/launch-x-tweaks.sh") != null);
            assertTrue(zf.getEntry("X-Tweaks/bin/launch-x-tweaks.bat") != null);
            assertTrue(zf.getEntry("X-Tweaks/payload/client-project-launcher.jar") != null);

            String installSh = new String(zf.getInputStream(zf.getEntry("X-Tweaks/install.sh")).readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(installSh.contains("rolling back"));

            String manifest = new String(zf.getInputStream(zf.getEntry("X-Tweaks/manifest.json")).readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(manifest.contains("\"installerVersion\""));
            assertTrue(manifest.contains("\"bundleEntries\""));
        }
    }
}
