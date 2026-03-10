package com.clientproject.launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RuntimePathsResolver {

    public List<Path> resolveLibraries(Path librariesDir) throws IOException {
        try (Stream<Path> stream = Files.walk(librariesDir)) {
            return stream
                    .filter(p -> p.toString().endsWith(".jar"))
                    .collect(Collectors.toList());
        }
    }

    public Path resolveVersionJar(Path gameDir, String version) {
        return gameDir
                .resolve("versions")
                .resolve(version)
                .resolve(version + ".jar");
    }
}
