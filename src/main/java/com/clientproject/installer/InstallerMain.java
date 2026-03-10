package com.clientproject.installer;

import java.nio.file.Path;

public final class InstallerMain {
    private InstallerMain() {
    }

    public static void main(String[] args) throws Exception {
        Path output = args.length > 0 ? Path.of(args[0]) : Path.of("target", "X-Tweaks-installer.zip");
        Path bundle = new InstallerPackager().createInstallerBundle(output);
        System.out.println(bundle.toAbsolutePath());
    }
}
