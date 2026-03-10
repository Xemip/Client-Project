package com.clientproject.launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class RuntimePathsResolver {

    public List<Path> resolveLibraries(Path libsDir) throws IOException {
        List<Path> libs = new ArrayList<>();

        if (Files.exists(libsDir)) {
            try (var stream = Files.walk(libsDir)) {
                stream
                        .filter(p -> Files.isRegularFile(p))
                        .filter(p -> p.toString().endsWith(".jar"))
                        .forEach(libs::add);
            }
        }

        return libs;
    }

    public Path resolveVersionJar(Path gameDir, String version) {
        return gameDir
                .resolve("versions")
                .resolve(version)
                .resolve(version + ".jar");
    }
}
