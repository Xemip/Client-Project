package com.clientproject.launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class RuntimePathsResolver {

    public List<Path> resolveLibraries(Path libsDir) throws IOException {
        List<Path> libraries = new ArrayList<>();

        if (!Files.exists(libsDir)) {
            return libraries;
        }

        try (var stream = Files.walk(libsDir)) {
            stream
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().endsWith(".jar"))
                .forEach(libraries::add);
        }

        return libraries;
    }

    public Path resolveVersionJar(Path gameDir, String version) {
        Path jar = gameDir
                .resolve("versions")
                .resolve(version)
                .resolve(version + ".jar");

        if (!Files.exists(jar)) {
            throw new IllegalStateException("Version jar not found: " + jar);
        }

        return jar;
    }
}
