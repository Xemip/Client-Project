package com.clientproject.installer;

import com.clientproject.branding.Branding;
import com.clientproject.client.ClientFeaturePack;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class InstallerPackager {
    private static final String ROOT = "X-Tweaks";

    public Path createInstallerBundle(Path outputZip) throws IOException {
        Path parent = outputZip.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        List<String> entries = new ArrayList<>();
        entries.add(ROOT + "/README.txt");
        entries.add(ROOT + "/install.sh");
        entries.add(ROOT + "/install.bat");
        entries.add(ROOT + "/uninstall.sh");
        entries.add(ROOT + "/uninstall.bat");
        entries.add(ROOT + "/bin/launch-x-tweaks.sh");
        entries.add(ROOT + "/bin/launch-x-tweaks.bat");
        entries.add(ROOT + "/payload/client-project-launcher.jar");

        InstallManifest manifest = new InstallManifest(
                Branding.CLIENT_NAME,
                Branding.TARGET_VERSION,
                "com.clientproject.launcher.LauncherMain",
                "prompt-4",
                Instant.now(),
                new ClientFeaturePack().enabledModuleNames(),
                entries
        );

        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(outputZip))) {
            putText(zip, ROOT + "/README.txt", readmeText());
            putText(zip, ROOT + "/manifest.json", toJson(manifest));
            putText(zip, ROOT + "/install.sh", installSh());
            putText(zip, ROOT + "/install.bat", installBat());
            putText(zip, ROOT + "/uninstall.sh", uninstallSh());
            putText(zip, ROOT + "/uninstall.bat", uninstallBat());
            putText(zip, ROOT + "/bin/launch-x-tweaks.sh", launcherSh());
            putText(zip, ROOT + "/bin/launch-x-tweaks.bat", launcherBat());
            putLauncherPayload(zip);
        }
        return outputZip;
    }

    private static void putLauncherPayload(ZipOutputStream zip) throws IOException {
        Path localJar = Path.of("target", "client-project-launcher.jar");
        byte[] bytes;
        if (Files.exists(localJar)) {
            bytes = Files.readAllBytes(localJar);
        } else {
            bytes = "placeholder launcher jar - build with Maven/package before distribution\n".getBytes(StandardCharsets.UTF_8);
        }
        zip.putNextEntry(new ZipEntry(ROOT + "/payload/client-project-launcher.jar"));
        zip.write(bytes);
        zip.closeEntry();
    }

    private static void putText(ZipOutputStream zip, String path, String content) throws IOException {
        zip.putNextEntry(new ZipEntry(path));
        zip.write(content.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    private static String readmeText() {
        return "X Tweaks hardened installer bundle for Minecraft 1.8.9\n"
                + "- Run install.sh (Linux/macOS) or install.bat (Windows)\n"
                + "- Launch using bin/launch-x-tweaks.[sh|bat]\n"
                + "- Uninstall using uninstall.sh or uninstall.bat\n";
    }

    private static String installSh() {
        return "#!/usr/bin/env bash\n"
                + "set -euo pipefail\n"
                + "SCRIPT_DIR=\"$(cd \"$(dirname \"${BASH_SOURCE[0]}\")\" && pwd)\"\n"
                + "TARGET_DIR=\"${HOME}/.x-tweaks\"\n"
                + "BACKUP_DIR=\"${TARGET_DIR}.backup.$(date +%s)\"\n"
                + "ROLLBACK=0\n"
                + "if [ -d \"${TARGET_DIR}\" ]; then cp -a \"${TARGET_DIR}\" \"${BACKUP_DIR}\"; ROLLBACK=1; fi\n"
                + "cleanup_on_error() {\n"
                + "  echo 'Install failed; rolling back.'\n"
                + "  rm -rf \"${TARGET_DIR}\"\n"
                + "  if [ \"${ROLLBACK}\" -eq 1 ]; then mv \"${BACKUP_DIR}\" \"${TARGET_DIR}\"; fi\n"
                + "}\n"
                + "trap cleanup_on_error ERR\n"
                + "mkdir -p \"${TARGET_DIR}/bin\"\n"
                + "cp \"${SCRIPT_DIR}/payload/client-project-launcher.jar\" \"${TARGET_DIR}/client-project-launcher.jar\"\n"
                + "cp \"${SCRIPT_DIR}/bin/launch-x-tweaks.sh\" \"${TARGET_DIR}/bin/launch-x-tweaks.sh\"\n"
                + "chmod +x \"${TARGET_DIR}/bin/launch-x-tweaks.sh\"\n"
                + "ln -sf \"${TARGET_DIR}/bin/launch-x-tweaks.sh\" \"${HOME}/Desktop/X-Tweaks.sh\" || true\n"
                + "trap - ERR\n"
                + "echo 'X Tweaks installed to' \"${TARGET_DIR}\"\n";
    }

    private static String uninstallSh() {
        return "#!/usr/bin/env bash\n"
                + "set -euo pipefail\n"
                + "TARGET_DIR=\"${HOME}/.x-tweaks\"\n"
                + "rm -rf \"${TARGET_DIR}\"\n"
                + "rm -f \"${HOME}/Desktop/X-Tweaks.sh\"\n"
                + "echo 'X Tweaks uninstalled.'\n";
    }

    private static String launcherSh() {
        return "#!/usr/bin/env bash\n"
                + "set -euo pipefail\n"
                + "ROOT=\"$(cd \"$(dirname \"${BASH_SOURCE[0]}\")/..\" && pwd)\"\n"
                + "java -jar \"${ROOT}/client-project-launcher.jar\" \"$@\"\n";
    }

    private static String installBat() {
        return "@echo off\r\n"
                + "setlocal enabledelayedexpansion\r\n"
                + "set SCRIPT_DIR=%~dp0\r\n"
                + "set TARGET_DIR=%USERPROFILE%\\.x-tweaks\r\n"
                + "set BACKUP_DIR=%TARGET_DIR%.backup\r\n"
                + "if exist \"%TARGET_DIR%\" (\r\n"
                + "  if exist \"%BACKUP_DIR%\" rmdir /s /q \"%BACKUP_DIR%\"\r\n"
                + "  xcopy /e /i /y \"%TARGET_DIR%\" \"%BACKUP_DIR%\" >nul\r\n"
                + ")\r\n"
                + "if not exist \"%TARGET_DIR%\\bin\" mkdir \"%TARGET_DIR%\\bin\"\r\n"
                + "copy /y \"%SCRIPT_DIR%payload\\client-project-launcher.jar\" \"%TARGET_DIR%\\client-project-launcher.jar\" >nul\r\n"
                + "if errorlevel 1 goto rollback\r\n"
                + "copy /y \"%SCRIPT_DIR%bin\\launch-x-tweaks.bat\" \"%TARGET_DIR%\\bin\\launch-x-tweaks.bat\" >nul\r\n"
                + "if errorlevel 1 goto rollback\r\n"
                + "copy /y \"%TARGET_DIR%\\bin\\launch-x-tweaks.bat\" \"%USERPROFILE%\\Desktop\\X-Tweaks.bat\" >nul\r\n"
                + "echo X Tweaks installed to %TARGET_DIR%\r\n"
                + "exit /b 0\r\n"
                + ":rollback\r\n"
                + "echo Install failed; rolling back.\r\n"
                + "rmdir /s /q \"%TARGET_DIR%\"\r\n"
                + "if exist \"%BACKUP_DIR%\" move \"%BACKUP_DIR%\" \"%TARGET_DIR%\" >nul\r\n"
                + "exit /b 1\r\n";
    }

    private static String uninstallBat() {
        return "@echo off\r\n"
                + "set TARGET_DIR=%USERPROFILE%\\.x-tweaks\r\n"
                + "if exist \"%TARGET_DIR%\" rmdir /s /q \"%TARGET_DIR%\"\r\n"
                + "if exist \"%USERPROFILE%\\Desktop\\X-Tweaks.bat\" del /f /q \"%USERPROFILE%\\Desktop\\X-Tweaks.bat\"\r\n"
                + "echo X Tweaks uninstalled.\r\n";
    }

    private static String launcherBat() {
        return "@echo off\r\n"
                + "set ROOT=%~dp0..\r\n"
                + "java -jar \"%ROOT%\\client-project-launcher.jar\" %*\r\n";
    }

    private static String toJson(InstallManifest manifest) {
        return "{\n"
                + "  \"clientName\": \"" + escape(manifest.clientName()) + "\",\n"
                + "  \"minecraftVersion\": \"" + escape(manifest.minecraftVersion()) + "\",\n"
                + "  \"launcherMainClass\": \"" + escape(manifest.launcherMainClass()) + "\",\n"
                + "  \"installerVersion\": \"" + escape(manifest.installerVersion()) + "\",\n"
                + "  \"builtAtUtc\": \"" + manifest.builtAtUtc() + "\",\n"
                + "  \"includedModules\": [" + modules(manifest.includedModules()) + "],\n"
                + "  \"bundleEntries\": [" + modules(manifest.bundleEntries()) + "]\n"
                + "}\n";
    }

    private static String modules(List<String> values) {
        return values.stream().map(name -> "\"" + escape(name) + "\"").reduce((a, b) -> a + ", " + b).orElse("");
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
