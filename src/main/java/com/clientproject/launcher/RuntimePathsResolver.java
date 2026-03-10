package com.clientproject.launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class RuntimePathsResolver {
    public Path resolveVersionJar(Path gameDirectory, String minecraftVersion) {
        return gameDirectory.resolve("versions").resolve(minecraftVersion).resolve(minecraftVersion + ".jar");
    }

    public List<Path> resolveLibraries(Path librariesDirectory) throws IOException {
        if (!Files.exists(librariesDirectory)) {
            return List.of();
        }
        List<Path> jars = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(librariesDirectory)) {
            stream.filter(path -> path.toString().endsWith(".jar"))
                    .sorted(Comparator.comparing(Path::toString))
                    .forEach(jars::add);
        }
        return jars;
    }
}
