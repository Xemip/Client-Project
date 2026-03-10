package com.clientproject.launcher;

import java.nio.file.Path;
import java.util.List;

public record LaunchPlan(Path workingDirectory, List<String> command) {
}
