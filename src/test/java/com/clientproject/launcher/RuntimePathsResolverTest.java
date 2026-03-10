package com.clientproject.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

final class RuntimePathsResolverTest {
    @Test
    void resolvesLibrariesAndVersionJar() throws Exception {
        Path root = Files.createTempDirectory("xtweaks-runtime");
        Path libs = root.resolve("libraries").resolve("a");
        Files.createDirectories(libs);
        Files.createFile(libs.resolve("one.jar"));
        Files.createFile(libs.resolve("two.jar"));

        RuntimePathsResolver resolver = new RuntimePathsResolver();
        List<Path> jars = resolver.resolveLibraries(root.resolve("libraries"));

        assertEquals(2, jars.size());
        Path versionJar = resolver.resolveVersionJar(root, "1.8.9");
        assertTrue(versionJar.toString().endsWith("versions/1.8.9/1.8.9.jar"));
    }
}
