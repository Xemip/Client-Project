package com.clientproject.launcher;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

class RuntimePathsResolverTest {

    @Test
    void resolvesLibrariesAndVersionJar() throws Exception {
        Path root = Files.createTempDirectory("resolver-test");

        Path libs = root.resolve("libs");
        Files.createDirectories(libs);

        Path libJar = libs.resolve("example.jar");
        Files.createFile(libJar);

        Path game = root.resolve("game");
        Path versionDir = game.resolve("versions").resolve("1.8.9");
        Files.createDirectories(versionDir);

        Path versionJar = versionDir.resolve("1.8.9.jar");
        Files.createFile(versionJar);

        RuntimePathsResolver resolver = new RuntimePathsResolver();

        List<Path> libraries = resolver.resolveLibraries(libs);
        Path resolvedJar = resolver.resolveVersionJar(game, "1.8.9");

        assertTrue(libraries.contains(libJar));
        assertTrue(resolvedJar.equals(versionJar));
    }
}
